package org.ecommerce.facade.api;

import org.ecommerce.dto.OrderRequest;
import org.ecommerce.dto.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderFacade
{
    OrderResponse placeOrder(String email, OrderRequest request,UUID couponId);

    List<OrderResponse> getCustomerOrders(String email);

    OrderResponse getCustomerOrder(String email, UUID orderId);

    List<OrderResponse> getAllOrders(String email);

    OrderResponse getOrderByOrderNumber(String email, String orderNumber);
}
