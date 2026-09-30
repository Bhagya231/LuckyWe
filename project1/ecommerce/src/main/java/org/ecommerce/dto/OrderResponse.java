package org.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse
{
    private UUID orderId;

    private UUID  userId;

    private String customerName;

    private String orderNumber;

    private String customerEmail;

    private String phone;

    private LocalDate orderDate;
    private LocalDate deliveryDate;

    private BigDecimal cost;

    private BigDecimal discount;

    private String address;


    private List<OrderItemsResponse> orderItems;
}
