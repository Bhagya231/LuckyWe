package org.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ecommerce.dto.WishlistRequest;
import org.ecommerce.dto.WishlistResponse;
import org.ecommerce.facade.api.WishlistFacade;
import org.ecommerce.utility.AppLogger;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
@CrossOrigin
public class WishlistController
{

    private static final Logger log = AppLogger.getLogger(WishlistController.class);

    private final WishlistFacade wishlistFacade;


    @PostMapping("add")
    public ResponseEntity<WishlistResponse> addToWishlist(@RequestParam UUID userId,
                                                          @Valid @RequestBody WishlistRequest request)
    {
        log.info("Add to wishlist request received. User ID: {}, Product ID: {}", userId, request.getProductId());

        WishlistResponse response = wishlistFacade.addToWishlist(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("view")
    public ResponseEntity<List<WishlistResponse>> getWishlist(@RequestParam UUID userId)
    {
        log.info("Get wishlist request received. User ID: {}", userId);

        List<WishlistResponse> response = wishlistFacade.getWishlist(userId);

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromWishlist(@RequestParam UUID userId, @PathVariable UUID productId)
    {
        log.info("Remove from wishlist request received. User ID: {}, Product ID: {}", userId, productId);

        wishlistFacade.removeFromWishlist(userId, productId);

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{productId}/movetocart")
    public ResponseEntity<WishlistResponse> moveToCart(@RequestParam UUID userId, @PathVariable UUID productId)
    {
        log.info("Move wishlist product to cart request received. User ID: {}, Product ID: {}", userId, productId);

        WishlistResponse response = wishlistFacade.moveToCart(userId, productId);

        return ResponseEntity.ok(response);
    }


}
