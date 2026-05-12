package com.loomi.desafiotech.orders.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loomi.desafiotech.orders.api.dto.CreateOrderItemRequest;
import com.loomi.desafiotech.orders.api.dto.CreateOrderRequest;
import com.loomi.desafiotech.orders.domain.event.producer.OrderEventProducer;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.OrderItem;
import com.loomi.desafiotech.orders.domain.model.Product;
import com.loomi.desafiotech.orders.infrastructure.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderCreationService {

    private final OrderRepository orderRepository;
    private final ProductCatalogService productCatalogService;
    private final ObjectMapper objectMapper;
    private final OrderEventProducer orderEventProducer;

    public OrderCreationService(
            OrderRepository orderRepository,
            ProductCatalogService productCatalogService,
            ObjectMapper objectMapper,
            OrderEventProducer orderEventProducer
    ) {
        this.orderRepository = orderRepository;
        this.productCatalogService = productCatalogService;
        this.objectMapper = objectMapper;
        this.orderEventProducer = orderEventProducer;
    }

    @Transactional
    public Order create(CreateOrderRequest request) {
        Order order = new Order(request.customerId());

        for (CreateOrderItemRequest itemRequest : request.items()) {
            Product product = productCatalogService
                    .getAvailableProductByProductId(itemRequest.productId());

            OrderItem orderItem = new OrderItem(
                    product.getProductId(),
                    product.getName(),
                    product.getProductType(),
                    itemRequest.quantity(),
                    product.getPrice(),
                    convertMetadataToJson(itemRequest)
            );

            order.addItem(orderItem);
        }

        Order savedOrder = orderRepository.save(order);

        orderEventProducer.publishOrderCreated(savedOrder);

        return savedOrder;
    }

    private String convertMetadataToJson(CreateOrderItemRequest itemRequest) {
        if (itemRequest.metadata() == null || itemRequest.metadata().isEmpty()) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(itemRequest.metadata());
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Invalid item metadata");
        }
    }
}