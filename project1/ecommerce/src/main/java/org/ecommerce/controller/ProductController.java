package org.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ecommerce.dto.ProductPageResponse;
import org.ecommerce.dto.ProductRequest;
import org.ecommerce.dto.ProductResponse;
import org.ecommerce.facade.api.ProductFacade;
import org.ecommerce.utility.AppLogger;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin
public class ProductController
{

    private static final Logger log = AppLogger.getLogger(ProductController.class);

    private final ProductFacade productFacade;

    @PostMapping("/add")
    public ResponseEntity<ProductResponse> addProduct(@RequestParam String email, @Valid @RequestBody ProductRequest request)
    {
        log.info("Add product request received. Admin email: {}, Product: {}", email, request.getName());

        ProductResponse response = productFacade.addProduct(email, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ProductPageResponse> getProducts(@RequestParam(defaultValue = "0") int offset,
                                                                @RequestParam(defaultValue = "20") int limit,
                                                                 @RequestParam(required = false) String search,
                                                                 @RequestParam(required = false) String category,
                                                                 @RequestParam(required = false) BigDecimal minPrice,
                                                                 @RequestParam(required = false) BigDecimal maxPrice) {

        log.info("Request received to fetch products. offset: {}, limit: {}, search: {}, category: {}, minPrice: {}, maxPrice: {}",
                offset, limit, search, category, minPrice, maxPrice);

        ProductPageResponse response = productFacade.getProducts(offset, limit, search, category, minPrice, maxPrice);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        log.info("Request received to fetch all products");

        List<ProductResponse> responses = productFacade.getAllProducts();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {

        log.info("Request received to fetch product. Product ID: {}", id);
        ProductResponse response = productFacade.getProductById(id);


        return ResponseEntity.ok(response);
    }




    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@RequestParam String email, @PathVariable UUID id, @Valid @RequestBody ProductRequest request) {

        log.info("Update product request received. Admin email: {}, Product ID: {}", email, id);

        ProductResponse response = productFacade.updateProduct(email, id, request);


        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@RequestParam String email, @PathVariable UUID id) {

        log.info("Delete product request received. Admin email: {}, Product ID: {}", email, id);

        productFacade.deleteProduct(email, id);

        return ResponseEntity.noContent().build();
    }
}
