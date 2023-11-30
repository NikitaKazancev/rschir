package ru.nk.rschir.users;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nk.rschir.authentication.AuthService;
import ru.nk.rschir.components.carts.CartRepo;
import ru.nk.rschir.components.carts.dto.CartFullData;
import ru.nk.rschir.components.stocks.dto.StockProductFullData;
import ru.nk.rschir.general.requests.ServiceFunctions;
import ru.nk.rschir.types.ResponseWithStatus;
import ru.nk.rschir.types.StatusCode;
import ru.nk.rschir.users.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final AuthService authService;
    private final ServiceFunctions functions;
    private final CartRepo cartRepo;

    private ResponseWithStatus<UserDTO> getUserWithData(ResponseWithStatus<User> response, Long id) {
        List<Object[]> cartData = cartRepo.findByUserIdWithData(id);

        CartFullData cart = new CartFullData();
        int sum = 0;
        List<StockProductFullData> products = new ArrayList<>();
        for (Object[] productData : cartData) {
            int price = Integer.parseInt(String.valueOf(productData[3]));
            sum += price;

            products.add(StockProductFullData.builder()
                    .id(Long.parseLong(String.valueOf(productData[0])))
                    .name(String.valueOf(productData[1]))
                    .amount(Integer.parseInt(String.valueOf(productData[2])))
                    .price(price)
                    .build());
        }
        cart.setProducts(products);
        cart.setSum(sum);

        return ResponseWithStatus.create(
                response.getStatus(),
                UserDTO.create(response.getData(), cart)
        );
    }
    public ResponseWithStatus<UserDTO> findById(Long id, HttpServletRequest request) {
        ResponseWithStatus<User> response = functions.findByWithAuth(id, userRepo::findById, request);
        if (response.getData() == null) {
            return ResponseWithStatus.create(response.getStatus(), null);
        }

        return getUserWithData(response, id);
    }
    public ResponseWithStatus<UserDTO> findByEmail(String email, HttpServletRequest request) {
        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseWithStatus.create(404, null);
        }

        User dbUser = authService.getUserByHttpRequest(request);
        if (dbUser == null
                || (dbUser.getRole() != Role.ADMIN
                    && !Objects.equals(email, dbUser.getEmail()))
        ) {
            return ResponseWithStatus.create(403, null);
        }

        Long id = user.getId();
        ResponseWithStatus<User> response = ResponseWithStatus.create(200, user);
        return getUserWithData(response, id);
    }
    public ResponseWithStatus<List<UserDTO>> findAll(HttpServletRequest request) {
        ResponseWithStatus<List<User>> response = functions.findAllWithAuth(userRepo::findAll, request);
        if (response.getData() == null) {
            return ResponseWithStatus.empty(403);
        }

        return ResponseWithStatus.create(
                response.getStatus(),
                response.getData().stream().map(UserDTO::create).toList()
        );
    }
    public StatusCode save(User user, HttpServletRequest request) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }

        return functions.saveWithAuth(
                user,
                user.getEmail(),
                userRepo::findByEmail,
                userRepo::save,
                request
        );
    }
    public StatusCode change(User user, HttpServletRequest request) {
        User dbUser = authService.getUserByHttpRequest(request);

        if (dbUser == null) {
            return StatusCode.create(403);
        }

        dbUser.merge(user);
        userRepo.save(dbUser);
        return StatusCode.create(200);
    }
    public StatusCode deleteById(HttpServletRequest request) {
        User dbUser = authService.getUserByHttpRequest(request);

        if (dbUser == null) {
            return StatusCode.create(403);
        }

        userRepo.deleteById(dbUser.getId());
        return StatusCode.create(200);
    }
}
