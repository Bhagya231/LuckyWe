package org.ecommerce.repository;

import org.ecommerce.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, UUID>
{

    @Query(value = """
            SELECT *
                FROM wishlist
                    WHERE user_id = :userId
                        AND is_deleted = false
    """, nativeQuery = true)
    List<Wishlist> findByUserIdAndIsDeletedFalse(@Param("userId") String userId);

    @Query(value = """
            SELECT *
                FROM wishlist
                    WHERE user_id = :userId
                        AND product_id = :productId
                            And is_deleted = false
    """, nativeQuery = true)
    Optional<Wishlist> findByUserIdAndProductIdAndIsDeletedFalse(@Param("userId") String userId, @Param("productId") String productId);


}
