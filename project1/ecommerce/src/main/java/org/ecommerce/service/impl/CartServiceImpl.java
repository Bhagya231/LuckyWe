package org.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.ecommerce.dto.CartItemsResponse;
import org.ecommerce.dto.CartRequest;
import org.ecommerce.dto.CartResponse;
import org.ecommerce.exception.BadRequestException;
import org.ecommerce.exception.ResourceNotFoundException;
import org.ecommerce.mapper.CartItemsMapper;
import org.ecommerce.mapper.CartMapper;
import org.ecommerce.model.*;
import org.ecommerce.repository.*;
import org.ecommerce.service.api.CartService;
import org.ecommerce.utility.AppLogger;
import org.slf4j.Logger;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService
{
    private static final Logger log = AppLogger.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final CartItemsMapper cartItemsMapper;


    @Override
    public CartResponse addToCart(UUID userId, CartRequest request)
    {
        log.info("Adding product to cart. User ID: {}", userId);

        try {
            User user = userRepository.findById(userId).orElseThrow(() ->
                            new ResourceNotFoundException("User not found " ));

            Product product = productRepository.findByIdAndIsDeletedFalse(request.getProductId().toString())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found  "));

            if (request.getQuantity() <= 0) {
                throw new BadRequestException("Quantity must be greater than zero");
            }
            if (product.getStock() < request.getQuantity())
            {
                throw new BadRequestException("Insufficient stock");
            }

            Cart cart = cartRepository.findActiveCartByUserId(userId.toString())
                    .orElse(null);

            if (cart == null)
            {
                cart = new Cart();
                cart.setUser(user);
                cart = cartRepository.save(cart);
            }

            CartItems cartItem = cartItemsRepository
                    .findActiveCartItems(cart.getId().toString(), product.getId().toString())
                    .orElse(null);

            if (cartItem == null) {

                cartItem = new CartItems();
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                cartItem.setQuantity(request.getQuantity());
                cart.getCartItems().add(cartItem);

            } else {

                int quantity = cartItem.getQuantity()
                        + request.getQuantity();

                if (quantity > product.getStock()) {
                    throw new BadRequestException("Insufficient stock");
                }

                cartItem.setQuantity(quantity);
            }

            cartItemsRepository.save(cartItem);

            log.info("Product added to cart successfully");

            return getCartResponse(cart);

        } catch (ResourceNotFoundException | BadRequestException e)
        {
            log.warn("Unable to add product to cart: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Error while adding product to cart", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CartResponse getCart(UUID userId)
    {
        log.info("Fetching cart for user ID: {}", userId);

        try {
            Cart cart = cartRepository.findActiveCartByUserId(userId.toString()).orElseThrow(() ->
                            new ResourceNotFoundException("Cart not found for user ID: " + userId));




            return getCartResponse(cart);

        } catch (ResourceNotFoundException | BadRequestException e)
        {
            log.warn("Unable to get products in the cart: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Error while fetching cart for user ID: {}", userId, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CartResponse increaseQuantity(UUID userId, UUID productId)
    {
        log.info("Increasing product quantity. User ID: {}", userId);

        try {
            CartItems cartItem = getCartItem(userId, productId);

            Product product = cartItem.getProduct();

            if (cartItem.getQuantity() >= product.getStock())
            {
                throw new BadRequestException("Insufficient stock");
            }

            cartItem.setQuantity(cartItem.getQuantity() + 1);

            cartItemsRepository.save(cartItem);

            return getCartResponse(cartItem.getCart());

        } catch (ResourceNotFoundException | BadRequestException e)
        {
            log.warn("Unable to increase quantity of product to cart: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Error while increasing quantity", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CartResponse decreaseQuantity(UUID userId, UUID productId) {

        log.info("Decreasing product quantity. User ID: {}", userId);

        try {
            CartItems cartItem = getCartItem(userId, productId);

            Cart cart = cartItem.getCart();

            if (cartItem.getQuantity() > 1)
            {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartItemsRepository.save(cartItem);

            } else {
                cartItem.setIsDeleted(true);
                cartItemsRepository.save(cartItem);
            }

            return getCartResponse(cart);

        } catch (ResourceNotFoundException e)
        {
            log.warn("Unable to decrease quantity of product to cart: {}", e.getMessage());
            throw e;

        } catch (Exception e)
        {
            log.error("Error while decreasing quantity", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void removeFromCart(UUID userId, UUID productId)
    {
        log.info("Removing product from cart. User ID: {}", userId);

        try {
            CartItems cartItem = getCartItem(userId, productId);

            cartItem.setIsDeleted(true);
            cartItemsRepository.save(cartItem);

            log.info("Product removed from cart successfully");

        } catch (ResourceNotFoundException e)
        {
            log.warn("Unable to remove the product from cart: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("Error while removing product from cart", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void clearCart(UUID userId)
    {
        log.info("Clearing cart for user ID: {}", userId);

        try {
            Cart cart = cartRepository.findActiveCartByUserId(userId.toString())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Cart not found for user ID: " + userId));

            for (CartItems cartItem : cart.getCartItems())
            {
                cartItem.setIsDeleted(true);
            }

            cartItemsRepository.saveAll(cart.getCartItems());

            log.info("Cart cleared successfully for user ID: {}", userId);

        } catch (ResourceNotFoundException e)
        {
            log.warn("Unable to clear cart: {}", e.getMessage());
            throw e;

        } catch (Exception e)
        {
            log.error("Error while clearing cart for user ID: {}", userId, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private CartItems getCartItem(UUID userId, UUID productId)
    {
        Cart cart = cartRepository.findActiveCartByUserId(userId.toString()).orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));

        return cartItemsRepository.findActiveCartItems(cart.getId().toString(), productId.toString())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));
    }

    private CartResponse getCartResponse(Cart cart)
    {
        CartResponse response = cartMapper.toResponse(cart);


        BigDecimal subtotal = BigDecimal.ZERO;
        List<CartItemsResponse> items = new ArrayList<>();


        for (CartItems cartItem : cart.getCartItems())
        {
            if (cartItem.getIsDeleted())
            {
                continue;
            }

            CartItemsResponse item = cartItemsMapper.toResponse(cartItem);


            BigDecimal itemTotal = item.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            item.setItemTotal(itemTotal);

            subtotal = subtotal.add(itemTotal);

            items.add(item);
        }

        BigDecimal discount = BigDecimal.ZERO;
        response.setCartItems(items);
        response.setDiscount(discount);
        response.setFinalTotal(subtotal);

        return response;
    }


}
