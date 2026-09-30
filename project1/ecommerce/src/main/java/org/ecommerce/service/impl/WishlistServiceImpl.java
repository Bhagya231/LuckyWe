package org.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.ecommerce.dto.CartRequest;
import org.ecommerce.dto.WishlistRequest;
import org.ecommerce.dto.WishlistResponse;
import org.ecommerce.exception.ResourceNotFoundException;
import org.ecommerce.mapper.WishlistMapper;
import org.ecommerce.model.Product;
import org.ecommerce.model.User;
import org.ecommerce.model.Wishlist;
import org.ecommerce.repository.ProductRepository;
import org.ecommerce.repository.UserRepository;
import org.ecommerce.repository.WishlistRepository;
import org.ecommerce.service.api.CartService;
import org.ecommerce.service.api.WishlistService;
import org.ecommerce.utility.AppLogger;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService
{
    private static final Logger log = AppLogger.getLogger(WishlistServiceImpl.class);

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final WishlistMapper wishlistMapper;


    @Override
    public WishlistResponse addToWishlist(UUID userId, WishlistRequest request)
    {
        log.info("Adding product to wishlist. User ID: {}, Product ID: {}", userId, request.getProductId());

        User user = userRepository.findById(userId).orElseThrow(() ->
                        new ResourceNotFoundException("User not found "));

        Product product = productRepository.findByIdAndIsDeletedFalse(request.getProductId().toString())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found " ));

        Wishlist existingWishlist = wishlistRepository
                .findByUserIdAndProductIdAndIsDeletedFalse(userId.toString(), request.getProductId().toString()).orElse(null);

        if (existingWishlist != null)
        {
            log.info("Product already exists in wishlist. Product ID: {}", request.getProductId());

            return wishlistMapper.toResponse(existingWishlist);
        }

        Wishlist wishlist = new Wishlist();

        wishlist.setUser(user);
        wishlist.setProduct(product);

        Wishlist savedWishlist = wishlistRepository.save(wishlist);

        log.info("Product added to wishlist successfully. Wishlist ID: {}", savedWishlist.getId());

        return wishlistMapper.toResponse(savedWishlist);
    }


    @Override
    public List<WishlistResponse> getWishlist(UUID userId)
    {
        log.info("Fetching wishlist. User ID: {}", userId);

        userRepository.findById(userId).orElseThrow(() ->
                        new ResourceNotFoundException("User not found  "));

        List<Wishlist> wishlist = wishlistRepository.findByUserIdAndIsDeletedFalse(userId.toString());

        List<WishlistResponse> responses = new ArrayList<>();

        for (Wishlist item : wishlist)
        {
            responses.add(wishlistMapper.toResponse(item));
        }

        log.info("Wishlist fetched successfully. User ID: {}, Items: {}", userId, responses.size());

        return responses;
    }


    @Override
    public void removeFromWishlist(UUID userId, UUID productId)
    {
        log.info("Removing product from wishlist. User ID: {}, Product ID: {}", userId, productId);

        Wishlist wishlist = wishlistRepository
                .findByUserIdAndProductIdAndIsDeletedFalse(userId.toString(), productId.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in wishlist"));

        wishlist.setIsDeleted(true);

        wishlistRepository.save(wishlist);

        log.info("Product removed from wishlist successfully. Product ID: {}", productId);
    }

    @Transactional
    @Override
    public WishlistResponse moveToCart(UUID userId, UUID productId)
    {
        log.info("Moving product from wishlist to cart. User ID: {}, Product ID: {}", userId, productId);

        Wishlist wishlist = wishlistRepository
                .findByUserIdAndProductIdAndIsDeletedFalse(userId.toString(), productId.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in wishlist"));

        CartRequest cartRequest = new CartRequest();

        cartRequest.setProductId(productId);
        cartRequest.setQuantity(1);

        cartService.addToCart(userId, cartRequest);

        wishlist.setIsDeleted(true);
        wishlistRepository.save(wishlist);

        log.info("Product moved to cart successfully. Product ID: {}", productId);

        return wishlistMapper.toResponse(wishlist);
    }





}
