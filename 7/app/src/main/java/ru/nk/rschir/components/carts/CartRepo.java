package ru.nk.rschir.components.carts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<CartEntity, Long> {
    @Query(value = "" +
            "select products.id, " +
            "products.name, " +
            "cart_entity_products.amount, " +
            "products.price * cart_entity_products.amount " +
            "from cart_entity_products " +
            "join carts on carts.id = cart_entity_products.cart_entity_id " +
            "join products on products.id = cart_entity_products.product_id " +
            "where carts.user_id = :userId ", nativeQuery = true)
    List<Object[]> findByUserIdWithData(Long userId);
    Optional<CartEntity> findByUserId(Long userId);
}
