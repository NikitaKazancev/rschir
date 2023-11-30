package ru.nk.rschir.components.products;

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
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ControllerFunctions functions;

    @GetMapping
    public ResponseEntity<List<ProductEntity>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseWithStatus<ProductEntity>> findById(@PathVariable Long id) {
        return functions.responseWithStatus(id, productService::findById);
    }
    @PostMapping
    public ResponseEntity<StatusCode> save(
            @RequestBody ProductEntity product,
            HttpServletRequest request
    ) {
        return functions.statusCode(product, productService::save, request);
    }
    @PutMapping
    public ResponseEntity<StatusCode> change(
            @RequestBody ProductEntity product,
            HttpServletRequest request
    ) {
        return functions.statusCode(product, productService::change, request);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<StatusCode> deleteById(
            @PathVariable Long id,
            @NonNull HttpServletRequest request
    ) {
        return functions.statusCode(id, productService::deleteById, request);
    }
}
