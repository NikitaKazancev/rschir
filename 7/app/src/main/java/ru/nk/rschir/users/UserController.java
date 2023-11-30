package ru.nk.rschir.users;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nk.rschir.general.requests.ControllerFunctions;
import ru.nk.rschir.types.ResponseWithStatus;
import ru.nk.rschir.types.StatusCode;
import ru.nk.rschir.users.dto.UserDTO;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ControllerFunctions functions;

    @GetMapping
    public ResponseEntity<ResponseWithStatus<List<UserDTO>>> findAll(
            @NonNull HttpServletRequest request
    ) {
        return functions.responseWithStatus(request, userService::findAll);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWithStatus<UserDTO>> findById(
            @PathVariable Long id,
            @NonNull HttpServletRequest request
    ) {
        return functions.responseWithStatus(id, userService::findById, request);
    }
    @GetMapping(params = "email")
    public ResponseEntity<ResponseWithStatus<UserDTO>> findByEmail(
            @RequestParam String email,
            @NonNull HttpServletRequest request
    ) {
        return functions.responseWithStatus(email, userService::findByEmail, request);
    }
    @PostMapping
    public ResponseEntity<StatusCode> save(
            @RequestBody User user,
            @NonNull HttpServletRequest request
    ) {
        return functions.statusCode(user, userService::save, request);
    }
    @PutMapping
    public ResponseEntity<StatusCode> change(
            @RequestBody User user,
            @NonNull HttpServletRequest request
    ) {
        return functions.statusCode(user, userService::change, request);
    }
    @DeleteMapping
    public ResponseEntity<StatusCode> deleteById(@NonNull HttpServletRequest request) {
        return functions.statusCode(userService::deleteById, request);
    }
}
