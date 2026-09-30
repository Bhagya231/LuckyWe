package org.ecommerce.repository;

import org.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>
{


    @Query(value = """
            SELECT *
                FROM products
                    WHERE id = :id
                        AND is_deleted = false
    """, nativeQuery = true)
    Optional<Product> findByIdAndIsDeletedFalse(@Param("id") String id);



    @Query(value = """
            SELECT * 
                FROM products
                    WHERE is_deleted = false
                        AND (:search Is NULL OR name LIKE CONCAT('%',:search,'%'))
                            AND (:category IS NULL OR category = :category)
                                 AND (:minPrice IS NULL OR price >= :minPrice)
                                    AND (:maxPrice IS NULL OR price <= :maxPrice)
                                        ORDER BY created_at DESC
                                            LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Product> findProducts(@Param("search") String search ,
                               @Param("category") String category,
                               @Param("minPrice") BigDecimal minPrice,
                               @Param("maxPrice") BigDecimal maxPrice,
                               @Param("limit") int limit,
                               @Param("offset") int offset);


    List<Product> findByIsDeletedFalse();


}
