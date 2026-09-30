package org.ecommerce.mapper;

import org.ecommerce.dto.CartResponse;
import org.ecommerce.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CartItemsMapper.class)
public interface CartMapper {

    @Mapping(source = "id", target = "cartId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItems")
    @Mapping(target = "finalTotal", ignore = true)
    CartResponse toResponse(Cart cart);

}
