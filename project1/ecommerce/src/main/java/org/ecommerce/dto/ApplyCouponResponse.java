package org.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplyCouponResponse
{
    private boolean success;
    private String message;
    private BigDecimal finalTotal;
    private BigDecimal discount;
    private BigDecimal amountToBePaid;
}
