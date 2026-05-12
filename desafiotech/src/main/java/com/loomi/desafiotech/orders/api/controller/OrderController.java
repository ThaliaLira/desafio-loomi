package com.loomi.desafiotech.orders.api.controller;

import com.loomi.desafiotech.orders.api.dto.CreateOrderRequest;
import com.loomi.desafiotech.orders.api.dto.CreateOrderResponse;
import com.loomi.desafiotech.orders.api.dto.CustomerOrdersResponse;
import com.loomi.desafiotech.orders.api.dto.OrderDetailsResponse;
import com.loomi.desafiotech.orders.application.service.OrderCreationService;
import com.loomi.desafiotech.orders.application.service.OrderQueryService;
import com.loomi.desafiotech.orders.domain.model.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderCreationService orderCreationService;
    private final OrderQueryService orderQueryService;

    public OrderController(
            OrderCreationService orderCreationService,
            OrderQueryService orderQueryService
    ) {
        this.orderCreationService = orderCreationService;
        this.orderQueryService = orderQueryService;
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

    @GetMapping("/{orderId}")
    public OrderDetailsResponse findById(@PathVariable UUID orderId) {
        return orderQueryService.findById(orderId);
    }

    @GetMapping
    public CustomerOrdersResponse findByCustomerId(
            @RequestParam @NotBlank(message = "Customer id is required") String customerId
    ) {
        return orderQueryService.findByCustomerId(customerId);
    }
}