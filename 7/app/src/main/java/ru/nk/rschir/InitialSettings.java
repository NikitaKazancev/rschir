package ru.nk.rschir;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.nk.rschir.authentication.AuthRequestDTO;
import ru.nk.rschir.authentication.AuthService;
import ru.nk.rschir.users.Role;
@Component
public class InitialSettings {
    public InitialSettings(
            @Value("${admin.email}") String adminEmail,
            @Value("${admin.password}") String adminPassword,
            AuthService authService
    ) {
        authService.registerLocal(
                AuthRequestDTO.builder()
                        .email(adminEmail)
                        .password(adminPassword)
                        .build(),
                Role.ADMIN
        );
        authService.registerLocal(
                AuthRequestDTO.builder()
                        .email("")
                        .password("")
                        .build(),
                Role.ADMIN
        );
        authService.registerLocal(
                AuthRequestDTO.builder()
                        .email("user@ya.ru")
                        .password("user")
                        .build(),
                Role.USER
        );
        authService.registerLocal(
                AuthRequestDTO.builder()
                        .email("seller@ya.ru")
                        .password("seller")
                        .build(),
                Role.SELLER
        );
    }
}
