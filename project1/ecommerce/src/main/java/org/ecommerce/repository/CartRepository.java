package org.ecommerce.repository;

import org.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID>
{

    @Query(value = """
            SELECT c.*
                FROM cart c
                    JOIN user u ON c.user_id = u.id
                        WHERE c.user_id = :userId
                            AND u.is_deleted = false    
    """, nativeQuery = true)
    Optional<Cart> findActiveCartByUserId(@Param("userId") String userId);


}
