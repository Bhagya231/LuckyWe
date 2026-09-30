package org.ecommerce.mapper;

import org.ecommerce.dto.ProductRequest;
import org.ecommerce.dto.ProductResponse;
import org.ecommerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductRequest request);

    @Mapping(target = "id", ignore = true)
    void updateProduct(ProductRequest request, @MappingTarget Product product);

    ProductResponse toResponse(Product product);
}
