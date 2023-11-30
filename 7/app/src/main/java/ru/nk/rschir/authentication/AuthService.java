package ru.nk.rschir.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.nk.rschir.components.carts.CartEntity;
import ru.nk.rschir.components.carts.CartRepo;
import ru.nk.rschir.users.Role;
import ru.nk.rschir.users.User;
import ru.nk.rschir.users.UserRepo;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final CartRepo cartRepo;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8081";
    public AuthResponseDTO login(AuthRequestDTO data) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String body = "{\"email\": \""+data.getEmail()+"\", \"password\": \""+data.getPassword()+"\"}";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/auth/login", HttpMethod.POST, entity, String.class
        );

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(response.getBody().getBytes(), AuthResponseDTO.class);
        } catch (IOException e) {
            return AuthResponseDTO.builder().jwt(null).status(403).build();
        }
    }
    public AuthResponseDTO register(AuthRequestDTO data) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String body = "{\"email\": \""+data.getEmail()+"\", \"password\": \""+data.getPassword()+"\"}";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + "/auth/register", HttpMethod.POST, entity, String.class
        );

        ObjectMapper mapper = new ObjectMapper();
        try {
            AuthResponseDTO res = mapper.readValue(response.getBody().getBytes(), AuthResponseDTO.class);
            if (res.getJwt() == null) {
                return AuthResponseDTO.builder().jwt(null).status(403).build();
            }

            registerLocal(data, Role.USER);
            return res;
        } catch (IOException e) {
            return AuthResponseDTO.builder().jwt(null).status(403).build();
        }
    }
    public void registerLocal(AuthRequestDTO data, Role role) {
        User dbUser = userRepo.findByEmail(data.getEmail()).orElse(null);
        if (dbUser != null) {
            return;
        }

        User user = User.builder()
                .email(data.getEmail())
                .role(role)
                .build();

        User savedUser = userRepo.save(user);
        cartRepo.save(CartEntity.builder().userId(savedUser.getId()).build());
    }
    public User getUserByHttpRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String jwt = authHeader.substring(7);
        String res = restTemplate.getForObject(baseUrl + "/auth/users?jwt=" + jwt, String.class);
        JSONObject object = new JSONObject(res);

        String email = object.getString("email");
        return userRepo.findByEmail(email).orElse(null);
    }
    public boolean isNotAdmin(HttpServletRequest request) {
        User user = getUserByHttpRequest(request);
        if (user == null) {
            return true;
        }

        return user.getRole() != Role.ADMIN;
    }
    public boolean isSeller(HttpServletRequest request) {
        User user = getUserByHttpRequest(request);
        if (user == null) {
            return false;
        }

        return user.getRole() == Role.SELLER;
    }
    public boolean isUser(HttpServletRequest request) {
        User user = getUserByHttpRequest(request);
        System.out.println(user);
        if (user == null) {
            return false;
        }

        return user.getRole() == Role.USER;
    }
}





