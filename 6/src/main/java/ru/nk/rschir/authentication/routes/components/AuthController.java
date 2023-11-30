package ru.nk.rschir.authentication.routes.components;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nk.rschir.authentication.routes.dto.AuthRequestDTO;
import ru.nk.rschir.authentication.routes.dto.AuthResponseDTO;
import ru.nk.rschir.authentication.routes.dto.RegisterRequestDTO;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @RequestBody RegisterRequestDTO registerData
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
