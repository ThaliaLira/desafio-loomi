package com.loomi.desafiotech.orders.domain.event.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loomi.desafiotech.orders.application.service.OrderProcessingService;
import com.loomi.desafiotech.orders.domain.event.producer.OrderEventProducer;
import com.loomi.desafiotech.orders.domain.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderCreatedConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedConsumer.class);

    private final ObjectMapper objectMapper;
    private final OrderProcessingService orderProcessingService;
    private final OrderEventProducer orderEventProducer;

    public OrderCreatedConsumer(
            ObjectMapper objectMapper,
            OrderProcessingService orderProcessingService,
            OrderEventProducer orderEventProducer
    ) {
        this.objectMapper = objectMapper;
        this.orderProcessingService = orderProcessingService;
        this.orderEventProducer = orderEventProducer;
    }

    @KafkaListener(
            topics = "order-events",
            groupId = "order-processing-group"
    )
    public void consume(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            String eventType = root.path("eventType").asText();

            if (!"ORDER_CREATED".equals(eventType)) {
                log.debug("Ignoring event type={}", eventType);
                return;
            }

            String orderIdAsText = root.path("payload").path("orderId").asText();
            UUID orderId = UUID.fromString(orderIdAsText);

            log.info("ORDER_CREATED event consumed. orderId={}", orderId);

            Order processedOrder = orderProcessingService.process(orderId);

            orderEventProducer.publishOrderResult(processedOrder);

        } catch (Exception exception) {
            log.error("Error while consuming ORDER_CREATED event. message={}", message, exception);
            throw new RuntimeException(exception);
        }
    }
}