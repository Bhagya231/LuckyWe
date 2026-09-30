package org.ecommerce.repository;

import org.ecommerce.model.CouponUsed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface couponUsedRepository extends JpaRepository<CouponUsed, UUID> {

    @Query(value = """
            SELECT COUNT(*) >0
                FROM coupon_used
                    WHERE user_id = :userId
                        AND coupon_id = :couponId 
    """,nativeQuery = true)
    long findByUserIdAndCouponId(@Param("userId") String userId, @Param("couponId") String couponId);
}
