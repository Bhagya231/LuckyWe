package org.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemsResponse
{
    private UUID id;


    private UUID productId;

    private String productName;

    private String category;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal itemTotal;

    private Integer stock;
}
