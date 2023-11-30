package ru.nk.rschir.components.stocks.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockProduct {
    private Long productId;
    private int amount;

    @Override
    public String toString() {
        return "StockProduct{" +
                "productId=" + productId +
                ", amount=" + amount +
                '}';
    }
}
