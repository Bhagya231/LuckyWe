package org.ecommerce.mapper;

import org.ecommerce.dto.CartItemsResponse;
import org.ecommerce.model.CartItems;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemsMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.category", target = "category")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "product.stock", target = "stock")
    @Mapping(target = "itemTotal", ignore = true)
    CartItemsResponse toResponse(CartItems cartItems);


}
