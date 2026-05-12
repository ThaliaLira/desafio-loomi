package com.loomi.desafiotech.orders.api.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        String productId,
        String productName,
        String productType,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice,
        String metadata
) {
}