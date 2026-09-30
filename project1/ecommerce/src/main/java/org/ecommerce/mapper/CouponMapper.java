package org.ecommerce.mapper;

import org.ecommerce.dto.CouponResponse;
import org.ecommerce.model.Coupon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CouponMapper
{

    CouponResponse toResponse(Coupon coupon);

//    Coupon toEntity(CouponRequest request);
//

//
//    void updateEntity(Coupon coupon, CouponRequest request);

}
