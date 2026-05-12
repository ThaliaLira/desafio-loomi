package com.loomi.desafiotech.orders.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loomi.desafiotech.orders.api.exception.OrderProcessingException;
import com.loomi.desafiotech.orders.application.processor.PreOrderItemProcessor;
import com.loomi.desafiotech.orders.domain.enums.ProductType;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.OrderItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PreOrderItemProcessorTest {

    private final PreOrderItemProcessor processor =
            new PreOrderItemProcessor(new ObjectMapper());

    @Test
    void shouldProcessPreOrderWhenReleaseDateIsFuture() {
        Order order = new Order("customer-pre-001");

        String futureDate = LocalDate.now().plusMonths(6).toString();

        OrderItem item = new OrderItem(
                "PRE-PS6-001",
                "PlayStation 6",
                ProductType.PRE_ORDER,
                1,
                new BigDecimal("4999.00"),
                "{\"releaseDate\":\"" + futureDate + "\"}"
        );

        assertThatCode(() -> processor.process(order, item))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldFailWhenReleaseDateHasPassed() {
        Order order = new Order("customer-pre-002");

        OrderItem item = new OrderItem(
                "PRE-PS6-001",
                "PlayStation 6",
                ProductType.PRE_ORDER,
                1,
                new BigDecimal("4999.00"),
                "{\"releaseDate\":\"2020-01-01\"}"
        );

        assertThatThrownBy(() -> processor.process(order, item))
                .isInstanceOf(OrderProcessingException.class)
                .hasMessageContaining("Release date must be in the future");
    }
}