package ru.nk.rschir.documents.orders;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nk.rschir.components.carts.dto.CartPutResult;
import ru.nk.rschir.general.requests.ControllerFunctions;
import ru.nk.rschir.types.ResponseWithStatus;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final ControllerFunctions functions;
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ResponseWithStatus<List<OrderEntity>>> findAll(HttpServletRequest request) {
        return functions.responseWithStatus(request, orderService::findAll);
    }
    @PostMapping
    public ResponseEntity<CartPutResult> save(HttpServletRequest request) {
        CartPutResult result = orderService.save(request);
        return ResponseEntity.status(result.getStatus()).body(result);
    }
}
