package org.ecommerce.service.api;

import org.ecommerce.dto.OrderRequest;
import org.ecommerce.dto.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService
{
    OrderResponse placeOrder(UUID userId, OrderRequest request,UUID couponId);

    List<OrderResponse> getCustomerOrders(UUID userId);

    OrderResponse getCustomerOrder(UUID orderId, UUID userId);

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderByOrderNumber(String orderNumber);
}
