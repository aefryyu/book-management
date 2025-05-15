package com.ali.order.domain.event;

import com.ali.order.domain.model.Address;
import com.ali.order.domain.model.Customer;
import com.ali.order.domain.model.OrderItem;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderCreatedEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        LocalDateTime createdAt) {}
