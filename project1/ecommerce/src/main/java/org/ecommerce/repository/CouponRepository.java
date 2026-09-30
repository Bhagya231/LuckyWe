package org.ecommerce.repository;

import org.ecommerce.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID>
{
//    @Query(value = """
//            SELECT *
//                FROM coupons
//                    WHERE coupon_name = :name
//                        AND is_deleted = false
//    """,nativeQuery = true)
//    Optional<Coupon> findActiveCouponByName(@Param("name") String name);

    @Query(value = """
            SELECT *
                FROM coupons
                    WHERE is_deleted = false  AND is_active = true 
                    AND start_date <= CURRENT_TIMESTAMP AND end_date >= CURRENT_TIMESTAMP
    """,nativeQuery = true)
    List<Coupon> findAllActiveCoupons();



    @Query(value = """
            SELECT *
            FROM coupons
            WHERE id = :id
            AND is_deleted = false
            AND is_active = true
            AND start_date <= CURRENT_TIMESTAMP
            AND end_date >= CURRENT_TIMESTAMP
""", nativeQuery = true)
    Optional<Coupon> findValidCoupon(@Param("id") String id);


//    @Query(value = """
//            SELECT COUNT(*) > 0
//                FROM coupons
//                    WHERE coupon_name = :couponName
//                        AND is_deleted = false
//    """, nativeQuery = true)
//    boolean existsByCouponNameAndIsDeletedFals(@Param("couponName") String couponName);

//    @Query(value = """
//               SELECT *
//                   FROM coupons
//                       WHERE id = :id
//                           AND is_deleted = false
//    """, nativeQuery = true)
//    Optional<Coupon>  findByIdAndIsDeletedFalse(String id);
}
