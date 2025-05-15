package com.ali.notifications.domain.event;

import com.ali.notifications.domain.model.Address;
import com.ali.notifications.domain.model.Customer;
import com.ali.notifications.domain.model.OrderItem;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderCreatedEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        LocalDateTime createdAt) {}
