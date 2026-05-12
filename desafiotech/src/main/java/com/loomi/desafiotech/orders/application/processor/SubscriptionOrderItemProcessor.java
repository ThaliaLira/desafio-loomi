package com.loomi.desafiotech.orders.application.processor;

import com.loomi.desafiotech.orders.api.exception.OrderProcessingException;
import com.loomi.desafiotech.orders.domain.enums.Failure;
import com.loomi.desafiotech.orders.domain.enums.ProductType;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionOrderItemProcessor implements OrderItemProcessor {

    private static final Logger log =
            LoggerFactory.getLogger(SubscriptionOrderItemProcessor.class);

    @Override
    public ProductType supports() {
        return ProductType.SUBSCRIPTION;
    }

    @Override
    public void process(Order order, OrderItem item) {

        long subscriptionCount = order.getItems()
                .stream()
                .filter(i -> i.getProductType() == ProductType.SUBSCRIPTION)
                .count();

        if (subscriptionCount > 5) {

            throw new OrderProcessingException(
                    Failure.SUBSCRIPTION_LIMIT_EXCEEDED,
                    "Customer exceeded subscription limit"
            );
        }

        boolean hasBasic =
                order.getItems()
                        .stream()
                        .anyMatch(i ->
                                "SUB-BASIC-001".equals(i.getProductId())
                        );

        boolean hasEnterprise =
                order.getItems()
                        .stream()
                        .anyMatch(i ->
                                "SUB-ENTERPRISE-001".equals(i.getProductId())
                        );

        if (hasBasic && hasEnterprise) {

            throw new OrderProcessingException(
                    Failure.INCOMPATIBLE_SUBSCRIPTIONS,
                    "Basic and Enterprise plans are incompatible"
            );
        }

        log.info(
                "Subscription processed. orderId={}, productId={}",
                order.getId(),
                item.getProductId()
        );
    }
}