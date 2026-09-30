package org.ecommerce.service.api;

import org.ecommerce.dto.ProductPageResponse;
import org.ecommerce.dto.ProductRequest;
import org.ecommerce.dto.ProductResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProductService
{
    ProductResponse addProduct(ProductRequest request);


    ProductPageResponse getProducts(int offset,int limit, String search,
                                    String category, BigDecimal  minPrice,BigDecimal maxPrice);

    ProductResponse getProductById(UUID id);

    List<ProductResponse> getAllProducts();


    ProductResponse updateProduct(UUID id, ProductRequest request);

    void deleteProduct(UUID id);
}
