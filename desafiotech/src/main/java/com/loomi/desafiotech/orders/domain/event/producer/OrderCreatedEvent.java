package com.loomi.desafiotech.orders.domain.event.producer;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID eventId,
        String eventType,
        OffsetDateTime timestamp,
        com.loomi.desafiotech.orders.domain.event.producer.OrderCreatedPayload payload
) {
}