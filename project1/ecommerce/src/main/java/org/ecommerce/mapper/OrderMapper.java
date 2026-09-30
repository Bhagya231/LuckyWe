package org.ecommerce.mapper;

import org.ecommerce.dto.OrderResponse;
import org.ecommerce.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderItemsMapper.class)
public interface OrderMapper {

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "customerName")
    @Mapping(source = "user.email", target = "customerEmail")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(source = "orderItems", target = "orderItems")
    OrderResponse toResponse(Order order);
}
