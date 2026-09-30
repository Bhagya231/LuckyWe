package org.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.ecommerce.dto.CartRequest;
import org.ecommerce.dto.CartResponse;
import org.ecommerce.facade.api.CartFacade;
import org.ecommerce.utility.AppLogger;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@CrossOrigin
public class CartController
{
    private static final Logger log = AppLogger.getLogger(CartController.class);
    private final CartFacade cartFacade;

    @PostMapping("/{userId}")
    public CartResponse addToCart(@PathVariable UUID userId, @RequestBody CartRequest request)
    {
        log.info("Add to cart request received. User ID: {}, Product ID: {}",
                userId, request.getProductId());

        CartResponse response = cartFacade.addToCart(userId, request);

        log.info("Add to cart request completed successfully. User ID: {}",
                userId);

        return response;
    }


    @GetMapping("/{userId}")
    public CartResponse getCart(@PathVariable UUID userId)
    {
        log.info("Get cart request received. User ID: {}", userId);

        CartResponse response = cartFacade.getCart(userId);

        log.info("Get cart request completed successfully. User ID: {}", userId);

        return response;
    }

    @PutMapping("/{userId}/increase/{productId}")
    public CartResponse increaseQuantity(@PathVariable UUID userId, @PathVariable UUID productId)
    {
        log.info("Increase quantity request received. User ID: {}, Product ID: {}",
                userId, productId);

        CartResponse response = cartFacade.increaseQuantity(userId, productId);

        log.info("Increase quantity request completed successfully. User ID: {}, Product ID: {}",
                userId, productId);

        return response;
    }

    @PutMapping("/{userId}/decrease/{productId}")
    public CartResponse decreaseQuantity(@PathVariable UUID userId, @PathVariable UUID productId)
    {
        log.info("Decrease quantity request received. User ID: {}, Product ID: {}",
                userId, productId);

        CartResponse response = cartFacade.decreaseQuantity(userId, productId);

        log.info("Decrease quantity request completed successfully. User ID: {}, Product ID: {}",
                userId, productId);

        return response;
    }

    @DeleteMapping("/{userId}/remove/{productId}")
    public void removeFromCart(@PathVariable UUID userId, @PathVariable UUID productId)
    {
        log.info("Remove from cart request received. User ID: {}, Product ID: {}",
                userId, productId);

        cartFacade.removeFromCart(userId, productId);

        log.info("Remove from cart request completed successfully. User ID: {}, Product ID: {}",
                userId, productId);
    }

    @DeleteMapping("/{userId}/clear")
    public void clearCart(@PathVariable UUID userId)
    {
        log.info("Clear cart request received. User ID: {}", userId);

        cartFacade.clearCart(userId);

        log.info("Clear cart request completed successfully. User ID: {}", userId);
    }

}
