package com.ali.bookwebapp.web.controller;

import com.ali.bookwebapp.clients.orders.*;
import com.ali.bookwebapp.service.SecurityHelper;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderServiceClient orderServiceClient;
    private final SecurityHelper securityHelper;

    public OrderController(OrderServiceClient orderServiceClient, SecurityHelper securityHelper) {
        this.orderServiceClient = orderServiceClient;
        this.securityHelper = securityHelper;
    }

    @GetMapping("/cart")
    public String cart() {
        return "cart";
    }

    @PostMapping("/api/orders")
    @ResponseBody
    public OrderConfirmationDTO confirmationDTO(@Valid @RequestBody CreateOrderRequest orderRequest) {
        log.info("Creating order request {}", orderRequest);
        return orderServiceClient.createOrder(getHeaders(), orderRequest);
    }

    @GetMapping("/api/orders/{orderNumber}")
    public String showOrderDetails(@PathVariable String orderNumber, Model model) {
        model.addAttribute("orderNumber", orderNumber);
        return "order_details";
    }

    @GetMapping("/api/orders/{orderNumber}")
    @ResponseBody
    public OrderDto getOrder(@PathVariable String orderNumber) {
        log.info("Fetching data from order number {}", orderNumber);
        return orderServiceClient.getOrder(getHeaders(), orderNumber);
    }

    @GetMapping("/orders")
    public String showOrders() {
        return "orders";
    }

    @GetMapping("/api/orders")
    @ResponseBody
    public List<OrderSummary> getOrders() {
        log.info("Fetching order summaries");
        return orderServiceClient.getOrders(getHeaders());
    }

    private Map<String, ?> getHeaders() {
        String accessToken = securityHelper.getAccessToken();
        return Map.of("Authorization", "Bearer " + accessToken);
    }
}
