package org.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.ecommerce.dto.ApplyCouponRequest;
import org.ecommerce.dto.ApplyCouponResponse;
import org.ecommerce.dto.CouponResponse;
import org.ecommerce.facade.api.CouponFacade;
import org.ecommerce.utility.AppLogger;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
@CrossOrigin
public class CouponController
{
    private static final Logger log = AppLogger.getLogger(CouponController.class);

    private final CouponFacade couponFacade;

    @GetMapping
    public List<CouponResponse> finaAllActiveCoupons()
    {
        log.info("started getting all Active couponse ");
        return couponFacade.findAllActiveCoupons();

    }

    @PostMapping("/apply")
    public ApplyCouponResponse applyCoupon(@RequestParam String email, @RequestBody ApplyCouponRequest request)
    {
        return couponFacade.applyCoupon(email,request);
    }
}
