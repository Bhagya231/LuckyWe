package org.ecommerce.repository;

import org.ecommerce.model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, UUID>
{

    @Query(value = """
        SELECT ci.*
        FROM cart_items ci
        JOIN products p ON ci.product_id = p.id
        WHERE ci.cart_id = :cartId
                AND ci.product_id = :productId
                        AND ci.is_deleted = false
                                AND p.is_deleted = false
        """, nativeQuery = true)
    Optional<CartItems> findActiveCartItems(@Param("cartId") String cartId,
                                            @Param("productId") String productId);


    @Query(value = """
            SELECT SUM(ci.quantity * p.price)
                FROM cart_items ci
                    JOIN products p ON ci.product_id = p.id
                        WHERE ci.cart_id = :cartId AND ci.is_deleted = false
    """, nativeQuery = true)
    BigDecimal getCartTotal(@Param("cartId") String cartId);

}
