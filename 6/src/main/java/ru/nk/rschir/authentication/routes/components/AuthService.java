package ru.nk.rschir.authentication.routes.components;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nk.rschir.authentication.jwt.JwtService;
import ru.nk.rschir.authentication.routes.dto.AuthRequestDTO;
import ru.nk.rschir.authentication.routes.dto.AuthResponseDTO;
import ru.nk.rschir.authentication.routes.dto.RegisterRequestDTO;
import ru.nk.rschir.components.carts.CartEntity;
import ru.nk.rschir.components.carts.CartRepo;
import ru.nk.rschir.components.carts.CartService;
import ru.nk.rschir.users.Role;
import ru.nk.rschir.users.User;
import ru.nk.rschir.users.UserRepo;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final CartRepo cartRepo;

    public User getUserByHttpRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return userRepo
                    .findByEmail(jwtService.getEmail(authHeader.substring(7)))
                    .orElse(null);
        }

        return null;
    }

    public boolean isNotAdmin(HttpServletRequest request) {
        User user = getUserByHttpRequest(request);
        return user == null || user.getRole() != Role.ADMIN;
    }

    public AuthResponseDTO register(RegisterRequestDTO requestData) {
        return register(requestData, Role.USER);
    }
    public AuthResponseDTO register(RegisterRequestDTO requestData, Role role) {
        Optional<User> dbUser = userRepo.findByEmail(requestData.getEmail());
        if (dbUser.isPresent()) {
            return AuthResponseDTO.builder().jwt(null).status(409).build();
        }

        User user = User.builder()
                .firstname(requestData.getFirstname())
                .lastname(requestData.getLastname())
                .email(requestData.getEmail())
                .password(passwordEncoder.encode(requestData.getPassword()))
                .role(role)
                .build();

        User savedUser = userRepo.save(user);

        cartRepo.save(CartEntity.builder().userId(savedUser.getId()).build());

        return AuthResponseDTO.builder()
                .jwt(jwtService.createToken(user))
                .status(200)
                .build();
    }

    public AuthResponseDTO login(AuthRequestDTO authData) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authData.getEmail(),
                        authData.getPassword()
                )
        );

        User user = userRepo.findByEmail(authData.getEmail())
                .orElseThrow();

        return AuthResponseDTO.builder()
                .jwt(jwtService.createToken(user))
                .status(200)
                .build();
    }
}





