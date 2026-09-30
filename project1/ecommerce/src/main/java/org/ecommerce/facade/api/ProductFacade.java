package org.ecommerce.facade.api;

import org.ecommerce.dto.ProductPageResponse;
import org.ecommerce.dto.ProductRequest;
import org.ecommerce.dto.ProductResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ProductFacade
{
    ProductResponse addProduct(String email, ProductRequest request);

    ProductPageResponse getProducts(int offset, int limit, String search,
                                    String category, BigDecimal minPrice, BigDecimal maxPrice);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(UUID id);

    ProductResponse updateProduct(String email, UUID id, ProductRequest request);

    void deleteProduct(String email, UUID id);
}
