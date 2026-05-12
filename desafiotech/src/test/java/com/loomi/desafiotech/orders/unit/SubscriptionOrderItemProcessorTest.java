package com.loomi.desafiotech.orders.unit;

import com.loomi.desafiotech.orders.api.exception.OrderProcessingException;
import com.loomi.desafiotech.orders.application.processor.SubscriptionOrderItemProcessor;
import com.loomi.desafiotech.orders.domain.enums.ProductType;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.OrderItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubscriptionOrderItemProcessorTest {

    private final SubscriptionOrderItemProcessor processor = new SubscriptionOrderItemProcessor();

    @Test
    void shouldProcessValidSubscription() {
        Order order = new Order("customer-sub-001");

        OrderItem item = new OrderItem(
                "SUB-PREMIUM-001",
                "Premium Monthly",
                ProductType.SUBSCRIPTION,
                1,
                new BigDecimal("49.90"),
                "{}"
        );

        order.addItem(item);

        assertThatCode(() -> processor.process(order, item))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldFailWhenBasicAndEnterpriseAreInSameOrder() {
        Order order = new Order("customer-sub-002");

        OrderItem basic = new OrderItem(
                "SUB-BASIC-001",
                "Basic Monthly",
                ProductType.SUBSCRIPTION,
                1,
                new BigDecimal("19.90"),
                "{}"
        );

        OrderItem enterprise = new OrderItem(
                "SUB-ENTERPRISE-001",
                "Enterprise Plan",
                ProductType.SUBSCRIPTION,
                1,
                new BigDecimal("299.00"),
                "{}"
        );

        order.addItem(basic);
        order.addItem(enterprise);

        assertThatThrownBy(() -> processor.process(order, basic))
                .isInstanceOf(OrderProcessingException.class)
                .hasMessageContaining("Basic and Enterprise");
    }
}