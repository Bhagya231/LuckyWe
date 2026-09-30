package org.ecommerce.facade.impl;

import lombok.RequiredArgsConstructor;
import org.ecommerce.dto.CartRequest;
import org.ecommerce.dto.CartResponse;
import org.ecommerce.exception.AccessDeniedException;
import org.ecommerce.exception.ResourceNotFoundException;
import org.ecommerce.facade.api.CartFacade;
import org.ecommerce.model.User;
import org.ecommerce.repository.UserRepository;
import org.ecommerce.service.api.CartService;
import org.ecommerce.utility.AppLogger;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class CartFacadeImpl implements CartFacade
{
    private static final Logger log = AppLogger.getLogger(CartFacade.class);
    private final CartService cartService;
    private final UserRepository userRepository;

    @Override
    public CartResponse addToCart(UUID userId, CartRequest request)
    {
        log.info("Adding product to cart. User ID: {}", userId);
        checkCustomer(userId);
        CartResponse response = cartService.addToCart(userId, request);
        log.info("Product added to cart successfully. User ID: {}", userId);
        return response;
    }

    @Override
    public CartResponse getCart(UUID userId)
    {
        log.info("Fetching cart. User ID: {}",userId);
        checkCustomer(userId);
        return cartService.getCart(userId);
    }

    @Override
    public CartResponse increaseQuantity(UUID userId, UUID productId)
    {
        log.info("Increasing cart item quantity. User ID: {}, Product ID: {}",
                userId, productId);
        checkCustomer(userId);
        return cartService.increaseQuantity(userId, productId);
    }

    @Override
    public CartResponse decreaseQuantity(UUID userId, UUID productId)
    {
        log.info("Decreasing cart item quantity. User ID: {}, Product ID: {}",
                userId, productId);
        checkCustomer(userId);
        return cartService.decreaseQuantity(userId, productId);
    }

    @Override
    public void removeFromCart(UUID userId, UUID productId)
    {
        log.info("Removing product from cart. User ID: {}, Product ID: {}",
                userId, productId);
        checkCustomer(userId);
        cartService.removeFromCart(userId, productId);
    }

    @Override
    public void clearCart(UUID userId)
    {
        log.info("Clearing cart through facade. User ID: {}", userId);

        cartService.clearCart(userId);
    }

    private void checkCustomer(UUID userId)
    {
        log.info("Checking customer access. User ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (!"CUSTOMER".equalsIgnoreCase(user.getRole())) {
            throw new AccessDeniedException("Only customers can use cart");
        }
    }

}
