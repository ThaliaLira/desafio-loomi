package com.loomi.desafiotech.orders.application.service;

import com.loomi.desafiotech.orders.api.exception.OrderProcessingException;
import com.loomi.desafiotech.orders.application.processor.OrderItemProcessor;
import com.loomi.desafiotech.orders.domain.enums.ProductType;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.OrderItem;
import com.loomi.desafiotech.orders.infrastructure.repository.OrderRepository;
import com.loomi.desafiotech.orders.shared.exceptions.OrderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderProcessingService {

    private static final Logger log = LoggerFactory.getLogger(OrderProcessingService.class);

    private final OrderRepository orderRepository;
    private final Map<ProductType, OrderItemProcessor> processorsByType;

    public OrderProcessingService(
            OrderRepository orderRepository,
            List<OrderItemProcessor> processors
    ) {
        this.orderRepository = orderRepository;
        this.processorsByType = new EnumMap<>(ProductType.class);

        for (OrderItemProcessor processor : processors) {
            this.processorsByType.put(processor.supports(), processor);
        }
    }

    @Transactional
    public Order process(UUID orderId) {
        log.info("Starting async order processing. orderId={}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        try {
            for (OrderItem item : order.getItems()) {
                OrderItemProcessor processor = processorsByType.get(item.getProductType());

                if (processor == null) {
                    throw new IllegalStateException("No processor found for product type " + item.getProductType());
                }

                processor.process(order, item);
            }

            if (!order.getStatus().name().equals("PENDING_APPROVAL")) {
                order.markAsProcessed();
            }

            Order processedOrder = orderRepository.save(order);

            log.info(
                    "Order processed successfully. orderId={}, status={}",
                    processedOrder.getId(),
                    processedOrder.getStatus()
            );

            return processedOrder;

        } catch (OrderProcessingException exception) {
            order.markAsFailed(exception.getFailureReason());

            Order failedOrder = orderRepository.save(order);

            log.warn(
                    "Order processing failed. orderId={}, reason={}",
                    failedOrder.getId(),
                    exception.getFailureReason()
            );

            return failedOrder;
        }
    }
}