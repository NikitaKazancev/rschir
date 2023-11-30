package ru.nk.rschir.components.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nk.rschir.components.stocks.dto.StockProduct;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByName(String name);
}
