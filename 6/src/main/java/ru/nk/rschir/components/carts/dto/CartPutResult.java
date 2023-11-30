package ru.nk.rschir.components.carts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nk.rschir.components.stocks.dto.StockProduct;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartPutResult {
    private int status;
    private List<StockProduct> products;
}
