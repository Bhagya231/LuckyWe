package org.ecommerce.facade.api;

import org.ecommerce.dto.ApplyCouponRequest;
import org.ecommerce.dto.ApplyCouponResponse;
import org.ecommerce.dto.CouponResponse;

import java.util.List;

public interface CouponFacade
{
    List<CouponResponse> findAllActiveCoupons();

    ApplyCouponResponse applyCoupon(String email, ApplyCouponRequest request);
}
