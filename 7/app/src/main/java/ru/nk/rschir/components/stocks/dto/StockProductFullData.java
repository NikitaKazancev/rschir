package ru.nk.rschir.components.stocks.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockProductFullData {
    private Long id;
    private String name;
    private int amount;
    private int price;
}
