package org.ecommerce.mapper;

import org.ecommerce.dto.OrderItemsResponse;
import org.ecommerce.model.OrderItems;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemsMapper {

    @Mapping(source = "id", target = "orderItemId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    OrderItemsResponse toResponse(OrderItems orderItems);
}
