package com.loomi.desafiotech.orders.api.dto;

import java.util.List;

public record CustomerOrdersResponse(
        List<OrderSummaryResponse> orders
) {
}