package com.ali.order.domain.dto;

import com.ali.order.domain.model.OrderStatus;

public record OrderSummary(String orderNumber, OrderStatus status) {}
