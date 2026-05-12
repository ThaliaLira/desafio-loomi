package com.loomi.desafiotech.orders.api.controller;

import com.loomi.desafiotech.orders.api.dto.CreateOrderRequest;
import com.loomi.desafiotech.orders.api.dto.CreateOrderResponse;
import com.loomi.desafiotech.orders.application.service.OrderCreationService;
import com.loomi.desafiotech.orders.domain.model.Order;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderCreationService orderCreationService;

    public OrderController(OrderCreationService orderCreationService) {
        this.orderCreationService = orderCreationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse create(
            @Valid @RequestBody CreateOrderRequest request
    ) {

        Order order = orderCreationService.create(request);

        return new CreateOrderResponse(
                order.getId(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getCreatedAt()
        );
    }
}