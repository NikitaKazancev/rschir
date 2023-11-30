package ru.nk.rschir.components.carts;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nk.rschir.components.stocks.StockEntity;
import ru.nk.rschir.components.stocks.dto.StockProduct;
import ru.nk.rschir.types.EntityWithMerge;

import java.util.List;

@Data
@Entity
@Table(name = "carts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartEntity implements EntityWithMerge<CartEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private int sum;
    @ElementCollection
    private List<StockProduct> products;

    @Override
    public void merge(CartEntity inputEntity) {
        List<StockProduct> field = inputEntity.getProducts();
        if (field != null) {
            this.setProducts(field);
        }
    }
}
