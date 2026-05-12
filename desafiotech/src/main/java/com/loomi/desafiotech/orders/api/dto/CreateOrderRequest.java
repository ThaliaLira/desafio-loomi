package com.loomi.desafiotech.orders.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(

        @NotBlank(message = "Customer id is required")
        String customerId,

        @Valid
        @NotEmpty(message = "Order must have at least one item")
        List<CreateOrderItemRequest> items

) {
}