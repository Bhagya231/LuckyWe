package org.ecommerce.service.api;

import org.ecommerce.dto.ApplyCouponRequest;
import org.ecommerce.dto.ApplyCouponResponse;
import org.ecommerce.dto.CouponResponse;
import org.ecommerce.model.Coupon;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CouponService
{

    List<CouponResponse> findAllActiveCoupons();

    ApplyCouponResponse applyCoupon(String email, ApplyCouponRequest request);

    BigDecimal calculateDiscount(BigDecimal amount, Coupon coupon);


//    CouponResponse createCoupon(CouponRequest request);
//
//    CouponResponse getCouponByName(String name);
//
//    CouponResponse updateCoupon(UUID id, CouponRequest request);
//
//    void deleteCoupon(UUID id);
}
