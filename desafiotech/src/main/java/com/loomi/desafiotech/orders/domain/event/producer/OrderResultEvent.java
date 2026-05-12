package com.loomi.desafiotech.orders.domain.event.producer;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OrderResultEvent(
        UUID eventId,
        String eventType,
        OffsetDateTime timestamp,
        Object payload
) {
}