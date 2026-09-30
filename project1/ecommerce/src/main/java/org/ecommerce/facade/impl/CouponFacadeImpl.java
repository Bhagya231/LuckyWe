package org.ecommerce.facade.impl;

import lombok.RequiredArgsConstructor;
import org.ecommerce.dto.ApplyCouponRequest;
import org.ecommerce.dto.ApplyCouponResponse;
import org.ecommerce.dto.CouponResponse;
import org.ecommerce.facade.api.CouponFacade;
import org.ecommerce.service.api.CouponService;
import org.ecommerce.utility.AppLogger;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CouponFacadeImpl implements CouponFacade
{
    private static final Logger log = AppLogger.getLogger(OrderFacadeImpl.class);
    private final CouponService couponService;

    @Override
    public List<CouponResponse> findAllActiveCoupons()
    {
        log.info("getting the All active coupons");
        return couponService.findAllActiveCoupons();
    }

    @Override
    public ApplyCouponResponse applyCoupon(String email, ApplyCouponRequest request)
    {
        log.info(" started applying the coupon");
        return couponService.applyCoupon(email, request);
    }

}
