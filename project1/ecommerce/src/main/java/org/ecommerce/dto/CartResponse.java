package org.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartResponse
{
    private UUID cartId;

    private UUID userId;

    private List<CartItemsResponse> cartItems;

    private BigDecimal discount;
    private BigDecimal finalTotal;


}
