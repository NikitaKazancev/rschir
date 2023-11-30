package ru.nk.rschir.components.stocks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepo extends JpaRepository<StockEntity, Long> {
    Optional<StockEntity> findByName(String name);
    @Query(value = "select products_data.product_id as product_id, sum(products_data.amount) as amount " +
            "from stock_entity_products as products_data " +
            "where product_id in :productsIds " +
            "group by product_id", nativeQuery = true)
    List<Object[]> remainingGoodsByProductsIds(Collection<Long> productsIds);
    @Query(value = "select data.stock_entity_id, stocks.name, data.product_id, data.amount " +
            "from stock_entity_products as data " +
            "join stocks on stocks.id = data.stock_entity_id " +
            "where data.product_id in :productsIds " +
            "order by data.stock_entity_id", nativeQuery = true)
    List<Object[]> goodsOfEachStockByProductsIds(Collection<Long> productsIds);
}
