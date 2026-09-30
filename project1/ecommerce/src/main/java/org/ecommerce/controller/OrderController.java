package org.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ecommerce.dto.OrderRequest;
import org.ecommerce.dto.OrderResponse;
import org.ecommerce.facade.api.OrderFacade;
import org.ecommerce.model.Coupon;
import org.ecommerce.utility.AppLogger;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin
public class OrderController
{

    private static final Logger log = AppLogger.getLogger(OrderController.class);

    private final OrderFacade orderFacade;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestParam String email, @Valid @RequestBody OrderRequest request, @RequestParam UUID couponId) {

        log.info("Place order request received. Customer email: {}", email);
        OrderResponse response = orderFacade.placeOrder(email, request,couponId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/customer")
    public ResponseEntity<List<OrderResponse>> getCustomerOrders(@RequestParam String email) {
        log.info("Request received to fetch customer orders. Email: {}", email);
        List<OrderResponse> responses = orderFacade.getCustomerOrders(email);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/customer/{orderId}")
    public ResponseEntity<OrderResponse> getCustomerOrder(@RequestParam String email, @PathVariable UUID orderId) {

        log.info("Request received to fetch customer order. Email: {}, Order ID: {}", email, orderId);

        OrderResponse response = orderFacade.getCustomerOrder(email, orderId);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/admin")
    public ResponseEntity<List<OrderResponse>> getAllOrders(@RequestParam String email) {

        log.info("Admin request received to fetch all orders. Email: {}", email);

        List<OrderResponse> responses = orderFacade.getAllOrders(email);



        return ResponseEntity.ok(responses);
    }


    @GetMapping("/admin/{orderNumber}")
    public ResponseEntity<OrderResponse> getOrderByOrderNumber(@RequestParam String email, @PathVariable String orderNumber) {

        log.info("Admin request received to fetch order. Email: {}, Order ID: {}", email, orderNumber);

        OrderResponse response = orderFacade.getOrderByOrderNumber(email, orderNumber);

        return ResponseEntity.ok(response);
    }

}
