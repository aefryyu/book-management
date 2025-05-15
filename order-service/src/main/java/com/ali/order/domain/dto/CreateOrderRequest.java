package com.ali.order.domain.dto;

import com.ali.order.domain.model.Address;
import com.ali.order.domain.model.Customer;
import com.ali.order.domain.model.OrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record CreateOrderRequest(
        @Valid @NotEmpty(message = "Items can't be empty") Set<OrderItem> items,
        @Valid Customer customer,
        @Valid Address deliveryAddress) {}
