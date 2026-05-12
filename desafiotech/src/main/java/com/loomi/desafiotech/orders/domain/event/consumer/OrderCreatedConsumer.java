package com.loomi.desafiotech.orders.domain.event.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loomi.desafiotech.orders.application.service.OrderProcessingService;
import com.loomi.desafiotech.orders.domain.event.producer.OrderEventProducer;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.ProcessedEvent;
import com.loomi.desafiotech.orders.infrastructure.repository.ProcessedEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class OrderCreatedConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderCreatedConsumer.class);

    private final ObjectMapper objectMapper;
    private final OrderProcessingService orderProcessingService;
    private final OrderEventProducer orderEventProducer;
    private final ProcessedEventRepository processedEventRepository;

    public OrderCreatedConsumer(
            ObjectMapper objectMapper,
            OrderProcessingService orderProcessingService,
            OrderEventProducer orderEventProducer,
            ProcessedEventRepository processedEventRepository
    ) {
        this.objectMapper = objectMapper;
        this.orderProcessingService = orderProcessingService;
        this.orderEventProducer = orderEventProducer;
        this.processedEventRepository = processedEventRepository;
    }

    @Transactional
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

            UUID eventId = UUID.fromString(root.path("eventId").asText());
            UUID orderId = UUID.fromString(root.path("payload").path("orderId").asText());

            if (processedEventRepository.existsByEventId(eventId)) {
                log.info(
                        "Duplicated event ignored. eventId={}, orderId={}",
                        eventId,
                        orderId
                );
                return;
            }

            log.info(
                    "ORDER_CREATED event consumed. eventId={}, orderId={}",
                    eventId,
                    orderId
            );

            Order processedOrder = orderProcessingService.process(orderId);

            processedEventRepository.save(
                    new ProcessedEvent(eventId, eventType, orderId)
            );

            orderEventProducer.publishOrderResult(processedOrder);

        } catch (DataIntegrityViolationException exception) {
            log.warn("Duplicated event detected by database unique constraint. message={}", message);
        } catch (Exception exception) {
            log.error("Error while consuming ORDER_CREATED event. message={}", message, exception);
            throw new RuntimeException(exception);
        }
    }
}