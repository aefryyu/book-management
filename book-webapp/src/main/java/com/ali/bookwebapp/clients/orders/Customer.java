package com.ali.bookwebapp.clients.orders;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Customer(
        @NotBlank(message = "customer name is required") String name,
        @NotBlank(message = "customer email is required") @Email String email,
        @NotBlank(message = "customer phone is required") String phone) {}
