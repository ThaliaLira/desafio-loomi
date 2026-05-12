package com.loomi.desafiotech.orders.domain.event.producer;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OrderProcessedPayload(
        UUID orderId,
        OffsetDateTime processedAt
) {
}