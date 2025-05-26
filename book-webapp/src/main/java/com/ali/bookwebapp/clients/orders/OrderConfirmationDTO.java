package com.ali.bookwebapp.clients.orders;

public record OrderConfirmationDTO(String orderNumber, OrderStatus status) {}
