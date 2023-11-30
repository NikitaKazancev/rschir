package ru.nk.rschir.components.stocks;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nk.rschir.components.products.ProductRepo;
import ru.nk.rschir.components.stocks.dto.StockProduct;
import ru.nk.rschir.general.requests.ServiceFunctions;
import ru.nk.rschir.types.ResponseWithStatus;
import ru.nk.rschir.types.StatusCode;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepo stockRepo;
    private final ServiceFunctions functions;
    private final ProductRepo productRepo;
    private boolean fieldsNotExist(StockEntity stock) {
        for (StockProduct product : stock.getProducts()) {
            if (!productRepo.existsById(product.getProductId())) {
                return true;
            }
        }

        return false;
    }
    public ResponseWithStatus<List<StockEntity>> findAll(HttpServletRequest request) {
        return functions.findAllWithAuth(
                stockRepo::findAll,
                request
        );
    }
    public StatusCode save(StockEntity stock, HttpServletRequest request) {
        return functions.saveWithCheckFieldsWithAuth(
                stock,
                this::fieldsNotExist,
                stock.getName(),
                stockRepo::findByName,
                stockRepo::save,
                request
        );
    }
    public StatusCode change(StockEntity stock, HttpServletRequest request) {
        return functions.changeWithCheckFieldsWithAuth(
                stock,
                this::fieldsNotExist,
                stockRepo::findById,
                stock.getId(),
                stockRepo::save,
                request
        );
    }
}
