package com.loomi.desafiotech.orders.domain.event.producer;

import com.loomi.desafiotech.orders.domain.enums.OrderStatus;
import com.loomi.desafiotech.orders.domain.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
public class OrderEventProducer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);

    private static final String TOPIC_NAME = "order-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreated(Order order) {
        com.loomi.desafiotech.orders.events.producer.OrderCreatedEvent event = new com.loomi.desafiotech.orders.events.producer.OrderCreatedEvent(
                UUID.randomUUID(),
                "ORDER_CREATED",
                OffsetDateTime.now(),
                new com.loomi.desafiotech.orders.events.producer.OrderCreatedPayload(
                        order.getId(),
                        order.getCustomerId(),
                        order.getTotalAmount()
                )
        );

        kafkaTemplate.send(
                TOPIC_NAME,
                order.getId().toString(),
                event
        );

        log.info("ORDER_CREATED event published. orderId={}", order.getId());
    }

    public void publishOrderResult(Order order) {
        if (order.getStatus() == OrderStatus.PROCESSED) {
            publishOrderProcessed(order);
            return;
        }

        if (order.getStatus() == OrderStatus.FAILED) {
            publishOrderFailed(order);
            return;
        }

        if (order.getStatus() == OrderStatus.PENDING_APPROVAL) {
            publishOrderPendingApproval(order);
        }
    }

    private void publishOrderProcessed(Order order) {
        OrderResultEvent event = new OrderResultEvent(
                UUID.randomUUID(),
                "ORDER_PROCESSED",
                OffsetDateTime.now(),
                new OrderProcessedPayload(
                        order.getId(),
                        OffsetDateTime.now()
                )
        );

        kafkaTemplate.send(TOPIC_NAME, order.getId().toString(), event);

        log.info("ORDER_PROCESSED event published. orderId={}", order.getId());
    }

    private void publishOrderFailed(Order order) {
        OrderResultEvent event = new OrderResultEvent(
                UUID.randomUUID(),
                "ORDER_FAILED",
                OffsetDateTime.now(),
                new OrderFailedPayload(
                        order.getId(),
                        order.getFailureReason() == null ? "UNKNOWN_ERROR" : order.getFailureReason().name(),
                        OffsetDateTime.now()
                )
        );

        kafkaTemplate.send(TOPIC_NAME, order.getId().toString(), event);

        log.info(
                "ORDER_FAILED event published. orderId={}, reason={}",
                order.getId(),
                order.getFailureReason()
        );
    }

    private void publishOrderPendingApproval(Order order) {
        OrderResultEvent event = new OrderResultEvent(
                UUID.randomUUID(),
                "ORDER_PENDING_APPROVAL",
                OffsetDateTime.now(),
                new OrderProcessedPayload(
                        order.getId(),
                        OffsetDateTime.now()
                )
        );

        kafkaTemplate.send(TOPIC_NAME, order.getId().toString(), event);

        log.info("ORDER_PENDING_APPROVAL event published. orderId={}", order.getId());
    }
}