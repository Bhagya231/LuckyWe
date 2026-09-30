package org.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.ecommerce.dto.ProductPageResponse;
import org.ecommerce.dto.ProductRequest;
import org.ecommerce.dto.ProductResponse;
import org.ecommerce.exception.ResourceNotFoundException;
import org.ecommerce.mapper.ProductMapper;
import org.ecommerce.model.Product;
import org.ecommerce.repository.ProductRepository;
import org.ecommerce.service.api.ProductService;
import org.ecommerce.utility.AppLogger;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService
{
    private static final Logger log = AppLogger.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse addProduct(ProductRequest request) {

        log.info("Adding new product: {}", request.getName());

        try {
            Product product = productMapper.toEntity(request);
            Product savedProduct = productRepository.save(product);

            log.info("Product added successfully. Product ID: {}", savedProduct.getId());


            return productMapper.toResponse(savedProduct);

        } catch (Exception e) {
            log.error("Error while adding product: {}", request.getName(), e);

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ProductPageResponse getProducts(int offset, int limit,String search,
                                           String category,BigDecimal minPrice,BigDecimal maxPrice)
    {
        log.info("Fetching products with  offset: {}, limit: {}, search: {}, category: {}, minPrice: {}, maxPrice: {}",
                offset, limit, search, category, minPrice, maxPrice);

        try{
            if(offset < 0)
            {
                throw new IllegalArgumentException("Offset can not be negative");
            }
            if(limit <= 0)
            {
                throw new IllegalArgumentException("Limit must be greater than 0");

            }
            if(search != null && search.isBlank())
            {
                search = null;
            }
            if(category != null && category.isBlank())
            {
                category = null;
            }
            if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Minimum price cannot be negative");
            }

            if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Maximum price cannot be negative");
            }
            if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
                throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
            }

            List<Product> products = productRepository.findProducts(search, category, minPrice, maxPrice, limit+1 , offset);
            boolean hasNext = products.size() > limit;

            if(hasNext)
            {
                products = products.subList(0,limit);
            }
            List<ProductResponse> responses = new ArrayList<>();

            for(Product product : products)
            {
                responses.add(productMapper.toResponse(product));
            }
            log.info("Successfully fetched {} products. Has next: {}",
                    responses.size(), hasNext);

            return new ProductPageResponse(responses,limit,hasNext);
        }catch (IllegalArgumentException e)
        {
            log.info("IllegalArgumentException happend  " ,e);
            throw e;
        }
        catch (Exception e)
        {
            log.error("Error while fetching products", e);

            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all active products");
        try {
            List<Product> products = productRepository.findByIsDeletedFalse();
            List<ProductResponse> responses = new ArrayList<>();
            for (Product product : products) {
                ProductResponse response = new ProductResponse();
                BeanUtils.copyProperties(product, response);
                responses.add(response);
            }
            log.info("Successfully fetched {} active products", responses.size());
            return responses;
        } catch (Exception e) {
            log.error("Error while fetching all products", e);
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public ProductResponse getProductById(UUID id) {

        log.info("Fetching product with ID: {}", id);

        try {

            Product product = productRepository.findByIdAndIsDeletedFalse(id.toString())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found with ID: " + id));

            ProductResponse response = new ProductResponse();
            BeanUtils.copyProperties(product, response);
            log.info("Product fetched successfully. Product ID: {}", id);
            return response;
        } catch (ResourceNotFoundException e) {
            log.warn("Product not found. Product ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Error while fetching product with ID: {}", id, e);
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public ProductResponse updateProduct(UUID id, ProductRequest request) {
        log.info("Updating product with ID: {}", id);
        try {
            Product product = productRepository.findByIdAndIsDeletedFalse(id.toString())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                            "Product not found "));
            productMapper.updateProduct(request,product);
            Product updatedProduct = productRepository.save(product);
            ProductResponse response = productMapper.toResponse(updatedProduct);

            log.info("Product updated successfully. Product ID: {}", id);
            return response;

        } catch (ResourceNotFoundException e) {
            log.warn("Product not found for update. Product ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Error while updating product with ID: {}", id, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteProduct(UUID id) {

        log.info("Deleting product with ID: {}", id);

        try {
            Product product = productRepository.findByIdAndIsDeletedFalse(id.toString())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException("Product not found "));

            product.setIsDeleted(true);
            productRepository.save(product);
            log.info("Product deleted successfully. Product ID: {}", id);

        } catch (ResourceNotFoundException e) {
            log.warn("Product not found for deletion. Product ID: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Error while deleting product with ID: {}", id, e);
            throw new RuntimeException(e.getMessage());
        }
    }

}
