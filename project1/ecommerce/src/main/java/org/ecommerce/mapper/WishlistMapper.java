package org.ecommerce.mapper;

import org.ecommerce.dto.WishlistResponse;
import org.ecommerce.model.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WishlistMapper {

    @Mapping(source = "product.id",target = "productId")
    @Mapping(source = "product.name",target = "productName")
    @Mapping(source = "product.category", target = "category")
    @Mapping(source = "product.price",target = "price")
    @Mapping(source = "product.stock", target = "stock")
    @Mapping(source = "product.description", target = "description")
    WishlistResponse toResponse(Wishlist wishlist);


}
