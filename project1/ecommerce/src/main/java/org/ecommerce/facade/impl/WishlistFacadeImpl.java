package org.ecommerce.facade.impl;

import lombok.RequiredArgsConstructor;
import org.ecommerce.dto.WishlistRequest;
import org.ecommerce.dto.WishlistResponse;
import org.ecommerce.exception.AccessDeniedException;
import org.ecommerce.exception.ResourceNotFoundException;
import org.ecommerce.facade.api.WishlistFacade;
import org.ecommerce.model.User;
import org.ecommerce.repository.UserRepository;
import org.ecommerce.service.api.WishlistService;
import org.ecommerce.utility.AppLogger;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WishlistFacadeImpl implements WishlistFacade
{
    private static final Logger log = AppLogger.getLogger(WishlistFacadeImpl.class);

    private final WishlistService wishlistService;
    private final UserRepository userRepository;

    @Override
    public WishlistResponse addToWishlist(UUID userId, WishlistRequest request)
    {
        log.info("Customer adding product to wishlist. User ID: {}, Product ID: {}", userId, request.getProductId());

        checkCustomer(userId);

        WishlistResponse response = wishlistService.addToWishlist(userId, request);

        log.info("Product added to wishlist successfully. User ID: {}", userId);

        return response;
    }

    @Override
    public List<WishlistResponse> getWishlist(UUID userId)
    {
        log.info("Customer requesting wishlist. User ID: {}", userId);

        checkCustomer(userId);

        List<WishlistResponse> response =
                wishlistService.getWishlist(userId);

        log.info("Wishlist fetched successfully. User ID: {}, Items: {}",
                userId, response.size());

        return response;
    }


    @Override
    public void removeFromWishlist(UUID userId, UUID productId)
    {
        log.info("Customer removing product from wishlist. User ID: {}, Product ID: {}",
                userId, productId);

        checkCustomer(userId);

        wishlistService.removeFromWishlist(userId, productId);

        log.info("Product removed from wishlist successfully. Product ID: {}",
                productId);
    }

    @Override
    public WishlistResponse moveToCart(UUID userId, UUID productId)
    {
        log.info("Customer moving wishlist product to cart. User ID: {}, Product ID: {}",
                userId, productId);

        checkCustomer(userId);

        WishlistResponse response =
                wishlistService.moveToCart(userId, productId);

        log.info("Wishlist product moved to cart successfully. Product ID: {}",
                productId);

        return response;
    }

    private void checkCustomer(UUID userId)
    {
        log.info("Checking customer access. User ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId));

        if (!"CUSTOMER".equalsIgnoreCase(user.getRole()))
        {
            log.warn("Access denied. User is not a customer. User ID: {}",
                    userId);

            throw new AccessDeniedException(
                    "Access denied. Only customers can use wishlist");
        }

        log.info("Customer access verified. User ID: {}", userId);
    }




}
