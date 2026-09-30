package org.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ecommerce.model.DiscountType;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponse
{
    private UUID id;
    private String couponName;

    private BigDecimal minOrderAmount;
    private BigDecimal value;
    private DiscountType discountType;
    private boolean isActive;
}
