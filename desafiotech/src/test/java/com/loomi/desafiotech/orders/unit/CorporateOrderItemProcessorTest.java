package com.loomi.desafiotech.orders.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loomi.desafiotech.orders.api.exception.OrderProcessingException;
import com.loomi.desafiotech.orders.application.processor.CorporateOrderItemProcessor;
import com.loomi.desafiotech.orders.domain.enums.OrderStatus;
import com.loomi.desafiotech.orders.domain.enums.ProductType;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.OrderItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CorporateOrderItemProcessorTest {

    private final CorporateOrderItemProcessor processor =
            new CorporateOrderItemProcessor(new ObjectMapper());

    @Test
    void shouldMarkCorporateOrderAsPendingApprovalWhenTotalIsGreaterThanThreshold() {
        Order order = new Order("company-acme");

        OrderItem item = new OrderItem(
                "CORP-LICENSE-ENT",
                "Enterprise License",
                ProductType.CORPORATE,
                5,
                new BigDecimal("15000.00"),
                "{\"cnpj\":\"12.345.678/0001-90\"}"
        );

        order.addItem(item);

        processor.process(order, item);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING_APPROVAL);
    }

    @Test
    void shouldFailWhenCnpjIsMissing() {
        Order order = new Order("company-invalid");

        OrderItem item = new OrderItem(
                "CORP-LICENSE-ENT",
                "Enterprise License",
                ProductType.CORPORATE,
                1,
                new BigDecimal("15000.00"),
                "{}"
        );

        order.addItem(item);

        assertThatThrownBy(() -> processor.process(order, item))
                .isInstanceOf(OrderProcessingException.class)
                .hasMessageContaining("Invalid corporate data");
    }
}