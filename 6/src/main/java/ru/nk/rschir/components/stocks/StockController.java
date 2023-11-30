package ru.nk.rschir.components.stocks;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nk.rschir.general.requests.ControllerFunctions;
import ru.nk.rschir.types.ResponseWithStatus;
import ru.nk.rschir.types.StatusCode;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {
    private final ControllerFunctions functions;
    private final StockService stockService;

    @GetMapping
    public ResponseEntity<ResponseWithStatus<List<StockEntity>>> findAll(
            @NonNull HttpServletRequest request
    ) {
        return functions.responseWithStatus(request, stockService::findAll);
    }
    @PostMapping
    public ResponseEntity<StatusCode> save(
            @RequestBody StockEntity stock,
            @NonNull HttpServletRequest request
    ) {
        return functions.statusCode(stock, stockService::save, request);
    }
    @PutMapping
    public ResponseEntity<StatusCode> change(
            @RequestBody StockEntity stock,
            @NonNull HttpServletRequest request
    ) {
        return functions.statusCode(stock, stockService::change, request);
    }
}
