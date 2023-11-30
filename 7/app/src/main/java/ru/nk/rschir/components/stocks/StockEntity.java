package ru.nk.rschir.components.stocks;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nk.rschir.components.stocks.dto.StockProduct;
import ru.nk.rschir.types.EntityWithMerge;

import java.util.List;

@Data
@Entity
@Table(name = "stocks")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockEntity implements EntityWithMerge<StockEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ElementCollection
    private List<StockProduct> products;

    @Override
    public void merge(StockEntity inputEntity) {
        List<StockProduct> field = inputEntity.getProducts();
        if (field != null) {
            this.setProducts(field);
        }
    }
}
