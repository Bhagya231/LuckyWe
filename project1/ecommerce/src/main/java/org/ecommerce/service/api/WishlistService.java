package org.ecommerce.service.api;

import org.ecommerce.dto.WishlistRequest;
import org.ecommerce.dto.WishlistResponse;

import java.util.List;
import java.util.UUID;

public interface WishlistService
{
    WishlistResponse addToWishlist(UUID userId, WishlistRequest request);

    List<WishlistResponse> getWishlist(UUID userId);

    void removeFromWishlist(UUID userId, UUID productId);

    WishlistResponse moveToCart(UUID userId, UUID productId);
}
