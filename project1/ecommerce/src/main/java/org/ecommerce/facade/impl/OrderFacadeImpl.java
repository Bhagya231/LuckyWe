package org.ecommerce.facade.impl;

import lombok.RequiredArgsConstructor;
import org.ecommerce.dto.OrderRequest;
import org.ecommerce.dto.OrderResponse;
import org.ecommerce.exception.AccessDeniedException;
import org.ecommerce.exception.BadRequestException;
import org.ecommerce.exception.ResourceNotFoundException;
import org.ecommerce.facade.api.OrderFacade;
import org.ecommerce.model.User;
import org.ecommerce.repository.UserRepository;
import org.ecommerce.service.api.OrderService;
import org.ecommerce.utility.AppLogger;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade
{
    private static final Logger log = AppLogger.getLogger(OrderFacadeImpl.class);

    private final OrderService orderService;
    private final UserRepository userRepository;

    @Override
    public OrderResponse placeOrder(String email, OrderRequest request,UUID couponId) {
        log.info("Customer attempting to place order. Email: {}", email);
        try {
            User user = checkCustomer(email.trim());
            OrderResponse response = orderService.placeOrder(user.getId(), request,couponId);
            log.info("Order placed successfully. User ID: {}", user.getId());
            return response;
        } catch (Exception e) {
            log.error("Unexpected error while placing order. Email: {}", email, e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public List<OrderResponse> getCustomerOrders(String email) {
        log.info("Customer requesting orders. Email: {}", email);
        try {
            User user = checkCustomer(email.trim());
            List<OrderResponse> responses = orderService.getCustomerOrders(user.getId());
            log.info("Customer orders fetched successfully. User ID: {}", user.getId());
            return responses;
        } catch (Exception e) {
            log.error("Unexpected error while fetching customer orders. Email: {}", email, e);

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public OrderResponse getCustomerOrder(String email, UUID orderId) {
        log.info("Customer requesting order. Email: {}, Order ID: {}", email, orderId);
        try {
            User user = checkCustomer(email);
            OrderResponse response = orderService.getCustomerOrder(orderId, user.getId());
            log.info("Customer order fetched successfully. Order ID: {}", orderId);
            return response;
        } catch (AccessDeniedException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while fetching customer order. " + "Email: {}, Order ID: {}", email, orderId, e);

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<OrderResponse> getAllOrders(String email) {
        log.info("Admin requesting all orders. Email: {}", email);
        try {
            checkAdmin(email);
            List<OrderResponse> responses = orderService.getAllOrders();
            log.info("All orders fetched successfully by admin. Email: {}", email);
            return responses;
        } catch (AccessDeniedException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while fetching all orders. Email: {}", email, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public OrderResponse getOrderByOrderNumber(String email, String orderNumber) {
        log.info("Admin requesting order. Email: {}, Order ID: {}", email, orderNumber);
        try {
            checkAdmin(email);
            OrderResponse response = orderService.getOrderByOrderNumber(orderNumber);
            log.info("Order fetched successfully by admin. Order ID: {}", orderNumber);
            return response;
        } catch (AccessDeniedException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while fetching order. " + "Email: {}, Order ID: {}", email, orderNumber, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private User checkCustomer(String email) {
        log.info("Checking customer access for email: {}", email);
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            if (!"CUSTOMER".equals(user.getRole())) {
                log.warn("Access denied. User is not a customer. Email: {}", email);
                throw new AccessDeniedException("Access denied. Only customer can perform this operation");
            }
            log.info("Customer access verified successfully. Email: {}", email);
            return user;
        } catch (ResourceNotFoundException e) {
            log.warn("Customer check failed. User not found: {}", email);
            throw e;
        } catch (AccessDeniedException e) {
            log.warn("Customer access denied. Email: {}", email);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while checking customer access. Email: {}", email, e);
            throw new RuntimeException(e.getMessage());
        }
    }


    private User checkAdmin(String email) {

        log.info("Checking admin access for email: {}", email);
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() ->
                    new ResourceNotFoundException("User not found"));
            if (!"ADMIN".equals(user.getRole())) {
                log.warn("Access denied. User is not an admin. Email: {}", email);
                throw new AccessDeniedException("Access denied. Only admin can perform this operation");
            }
            log.info("Admin access verified successfully. Email: {}", email);
            return user;
        } catch (ResourceNotFoundException e) {
            log.warn("Admin check failed. User not found: {}", email);
            throw e;
        } catch (AccessDeniedException e) {
            log.warn("Admin access denied. Email: {}", email);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while checking admin access. Email: {}", email, e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
