package org.ecommerce.facade.api;

import org.ecommerce.dto.CartRequest;
import org.ecommerce.dto.CartResponse;

import java.util.UUID;

public interface CartFacade
{
    CartResponse addToCart(UUID userId, CartRequest request);

    CartResponse getCart(UUID userId);

    CartResponse increaseQuantity(UUID userId, UUID productId);

    CartResponse decreaseQuantity(UUID userId, UUID productId);

    void removeFromCart(UUID userId, UUID productId);

    void clearCart(UUID userId);
}
