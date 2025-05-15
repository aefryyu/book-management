package com.ali.order.web.controller;

import com.ali.order.domain.dto.CreateOrderRequest;
import com.ali.order.domain.dto.CreateOrderResponse;
import com.ali.order.domain.dto.OrderDto;
import com.ali.order.domain.dto.OrderSummary;
import com.ali.order.domain.exception.OrderNotFoundException;
import com.ali.order.domain.service.OrderService;
import com.ali.order.domain.service.SecurityService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final SecurityService securityService;

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        String userName = securityService.getLoginUserName();
        log.info("Create order for user : {}", userName);
        return orderService.createOrder(userName, request);
    }

    @GetMapping
    List<OrderSummary> getOrders() {
        String userName = securityService.getLoginUserName();
        log.info("Fetching orders for user : {}", userName);
        return orderService.findOrders(userName);
    }

    @GetMapping("/{orderNumber}")
    OrderDto getOrder(@PathVariable(value = "orderNumber") String orderNumber) {
        log.info("Fetching order for order number : {}", orderNumber);
        String userName = securityService.getLoginUserName();
        return orderService
                .findUserOrder(userName, orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
    }
}
