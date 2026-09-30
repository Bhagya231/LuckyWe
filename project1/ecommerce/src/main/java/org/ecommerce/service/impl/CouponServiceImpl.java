package org.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.ecommerce.dto.*;

import org.ecommerce.mapper.CouponMapper;
import org.ecommerce.model.Cart;
import org.ecommerce.model.Coupon;
import org.ecommerce.model.DiscountType;
import org.ecommerce.model.User;
import org.ecommerce.repository.*;
import org.ecommerce.service.api.CouponService;
import org.ecommerce.utility.AppLogger;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService
{
    private static final Logger logger = AppLogger.getLogger(CouponService.class);

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    private final UserRepository userRepository;
    private final CartRepository cartRepository ;
    private final CartItemsRepository cartItemsRepository;
    private final couponUsedRepository couponUsedRepository;



    @Override
    public List<CouponResponse> findAllActiveCoupons()
    {
        logger.info("stated fetching all active coupons ");
        List<Coupon> coupons = couponRepository.findAllActiveCoupons();

        return coupons.stream().map(couponMapper::toResponse).toList();

    }

    @Override
    public ApplyCouponResponse applyCoupon(String email, ApplyCouponRequest request)
    {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                                                    new RuntimeException("User not found"));

        Cart cart =  cartRepository.findActiveCartByUserId(user.getId().toString())
                .orElseThrow(() -> new RuntimeException("Cart not found"));



        BigDecimal cartTotal = cartItemsRepository.getCartTotal(cart.getId().toString());

        Coupon coupon = couponRepository.findValidCoupon(request.getCouponId().toString())
                .orElseThrow(() -> new RuntimeException("Coupon is not valid"));



        long alreadyUsed = couponUsedRepository.findByUserIdAndCouponId(user.getId().toString(),coupon.getId().toString());

        if (alreadyUsed > 0)
        {
            throw new RuntimeException("You have already used this coupon");
        }


        BigDecimal discount = calculateDiscount(cartTotal,coupon);
        BigDecimal total = cartTotal.subtract(discount);



        return  new ApplyCouponResponse(true,"coupon applied successfully",cartTotal,
                discount,
                total);
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal amount, Coupon coupon)
    {
        if(amount.compareTo(coupon.getMinOrderAmount()) < 0)
        {
            BigDecimal remaining = coupon.getMinOrderAmount().subtract(amount);

            throw  new RuntimeException("Shop ₹" + remaining +" more to use this coupon");

        }

        BigDecimal discount;

        if (coupon.getDiscountType() == DiscountType.PERCENTAGE)
        {
            discount = amount.multiply(coupon.getValue()).divide(BigDecimal.valueOf(100));
        }
        else
        {
            discount = coupon.getValue();
        }

        if (discount.compareTo(amount) > 0)
        {
            discount = amount;
        }

        return discount;
    }


//
//    public CouponResponse createCoupon(CouponRequest request)
//    {
//        logger.info("Creating coupon with name: {}", request.getCouponName());
//
//        if (couponRepository.existsByCouponNameAndIsDeletedFals(request.getCouponName()))
//        {
//            logger.warn("Coupon already exists: {}", request.getCouponName());
//            throw new CouponAlreadyExistsException("Coupon already exists  ");
//        }
//        try {
//            Coupon coupon = couponMapper.toEntity(request);
//            Coupon savedCoupon = couponRepository.save(coupon);
//
//            logger.info("Coupon created successfully with id: {}", savedCoupon.getId());
//
//            return couponMapper.toResponse(savedCoupon);
//        }
//        catch (Exception e)
//        {
//            logger.error("Error while creating coupon: {}", e.getMessage(), e);
//            throw e;
//        }
//    }
//
//    public CouponResponse getCouponByName(String name)
//    {
//        logger.info("Fetching coupon with id: {}", name);
//
//        try
//        {
//            Coupon coupon = couponRepository.findActiveCouponByName(name)
//                    .orElseThrow(() -> new CouponNotFoundException("Coupon not found "));
//
//            return couponMapper.toResponse(coupon);
//        }
//        catch (CouponNotFoundException e)
//        {
//            logger.warn("Coupon not found with id: {}", name);
//            throw e;
//        }
//        catch (Exception e)
//        {
//            logger.error("Error while fetching coupon: {}", e.getMessage(), e);
//            throw e;
//        }
//    }
//
//    public CouponResponse updateCoupon(UUID id, CouponRequest request)
//    {
//        logger.info("Updating coupon with id: {}", id);
//
//        try
//        {
//            Coupon coupon = couponRepository.findByIdAndIsDeletedFalse(id.toString())
//                    .orElseThrow(() -> new CouponNotFoundException("Coupon not found "));
//
//            if (!coupon.getCouponName().equals(request.getCouponName())
//                    && couponRepository.existsByCouponNameAndIsDeletedFals(request.getCouponName())) {
//                logger.warn("Another coupon already exists with name: {}", request.getCouponName());
//
//                throw new CouponAlreadyExistsException("Coupon already exists " );
//            }
//
//            couponMapper.updateEntity(coupon, request);
//
//            Coupon updatedCoupon = couponRepository.save(coupon);
//
//            logger.info("Coupon updated successfully with id: {}", id);
//
//            return couponMapper.toResponse(updatedCoupon);
//        }
//        catch (CouponNotFoundException | CouponAlreadyExistsException e)
//        {
//            throw e;
//        }
//        catch (Exception e)
//        {
//            logger.error("Error while updating coupon: {}", e.getMessage());
//            throw e;
//        }
//
//    }
//
//    public void deleteCoupon(UUID id)
//    {
//        logger.info("Deleting coupon with id: {}", id);
//
//        try
//        {
//            Coupon coupon = couponRepository.findByIdAndIsDeletedFalse(id.toString())
//                    .orElseThrow(() ->
//                            new CouponNotFoundException("Coupon not found " ));
//
//            coupon.setIsDeleted(true);
//
//            couponRepository.save(coupon);
//
//            logger.info("Coupon deleted successfully with id: {}", id);
//        }
//        catch (CouponNotFoundException e)
//        {
//            logger.warn("Coupon not found with id: {}", id);
//            throw e;
//        }
//        catch (Exception e)
//        {
//            logger.error("Error while deleting coupon: {}", e.getMessage(), e);
//            throw e;
//        }
//    }



}
