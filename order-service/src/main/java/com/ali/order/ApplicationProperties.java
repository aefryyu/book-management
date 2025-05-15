package com.ali.order;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "orders")
public record ApplicationProperties(
        String catalogServiceUrl,
        String orderEventExchange,
        String newOrdersQueue,
        String deliveredOrdersQueue,
        String canceledOrdersQueue,
        String errorOrdersQueue) {}
