package ru.nk.rschir.components.products;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nk.rschir.types.EntityWithMerge;

@Data
@Entity
@Table(name = "products")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity implements EntityWithMerge<ProductEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int price;

    @Override
    public void merge(ProductEntity inputEntity) {
        int field = inputEntity.getPrice();
        if (field != 0) {
            this.setPrice(field);
        }
    }
}
