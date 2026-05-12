package com.loomi.desafiotech.orders.domain.event.producer;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OrderFailedPayload(
        UUID orderId,
        String reason,
        OffsetDateTime failedAt
) {
}