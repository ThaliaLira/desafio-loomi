package com.loomi.desafiotech.orders.application.service;

import com.loomi.desafiotech.orders.api.dto.CustomerOrdersResponse;
import com.loomi.desafiotech.orders.api.dto.OrderDetailsResponse;
import com.loomi.desafiotech.orders.api.dto.OrderItemResponse;
import com.loomi.desafiotech.orders.api.dto.OrderSummaryResponse;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.OrderItem;
import com.loomi.desafiotech.orders.infrastructure.repository.OrderRepository;
import com.loomi.desafiotech.orders.shared.exceptions.OrderNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public OrderQueryService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public OrderDetailsResponse findById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return toDetailsResponse(order);
    }

    @Transactional(readOnly = true)
    public CustomerOrdersResponse findByCustomerId(String customerId) {
        List<OrderSummaryResponse> orders = orderRepository.findByCustomerId(customerId)
                .stream()
                .map(this::toSummaryResponse)
                .toList();

        return new CustomerOrdersResponse(orders);
    }

    private OrderDetailsResponse toDetailsResponse(Order order) {
        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(this::toItemResponse)
                .toList();

        return new OrderDetailsResponse(
                order.getId(),
                order.getCustomerId(),
                items,
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getFailureReason() == null ? null : order.getFailureReason().name(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getProductId(),
                item.getProductName(),
                item.getProductType().name(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice(),
                item.getMetadata()
        );
    }

    private OrderSummaryResponse toSummaryResponse(Order order) {
        return new OrderSummaryResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getCreatedAt()
        );
    }
}