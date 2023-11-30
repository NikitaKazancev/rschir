package ru.nk.rschir.components.carts.dto;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nk.rschir.components.stocks.dto.StockProductFullData;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartFullData {
    private int sum;
    @ElementCollection
    private List<StockProductFullData> products;
}
