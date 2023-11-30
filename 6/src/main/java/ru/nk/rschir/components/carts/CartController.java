package ru.nk.rschir.components.carts;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nk.rschir.components.carts.dto.CartPutResult;
import ru.nk.rschir.general.requests.ControllerFunctions;
import ru.nk.rschir.types.StatusCode;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {
    private final ControllerFunctions functions;
    private final CartService cartService;
    @PostMapping
    public ResponseEntity<StatusCode> save(
            @RequestBody CartEntity cart,
            HttpServletRequest request
    ) {
        StatusCode res = cartService.save(cart, request);
        return ResponseEntity.status(res.getStatus()).body(res);
    }
    @PutMapping
    public ResponseEntity<CartPutResult> change(
            @RequestBody CartEntity cart,
            HttpServletRequest request
    ) {
        CartPutResult result = cartService.change(cart, request);
        return ResponseEntity.status(result.getStatus()).body(result);
    }
}
