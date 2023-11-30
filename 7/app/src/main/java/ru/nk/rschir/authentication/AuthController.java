package ru.nk.rschir.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @RequestBody AuthRequestDTO registerData
    ) {
        AuthResponseDTO response = authService.register(registerData);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody AuthRequestDTO authData
    ) {
        return ResponseEntity.ok(authService.login(authData));
    }
}