package com.ali.notifications;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notifications")
public record ApplicationProperties(
        String supportEmail,
        String orderEventExchange,
        String newOrdersQueue,
        String deliveredOrdersQueue,
        String canceledOrdersQueue,
        String errorOrdersQueue) {}
