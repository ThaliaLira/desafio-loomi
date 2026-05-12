package com.loomi.desafiotech.orders.domain.event.producer;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderCreatedPayload(
        UUID orderId,
        String customerId,
        BigDecimal totalAmount
) {
}