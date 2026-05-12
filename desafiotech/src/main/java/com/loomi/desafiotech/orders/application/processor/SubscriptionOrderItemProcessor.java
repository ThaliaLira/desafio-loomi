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

    private static final Logger log = LoggerFactory.getLogger(SubscriptionOrderItemProcessor.class);

    @Override
    public ProductType supports() {
        return ProductType.SUBSCRIPTION;
    }

    @Override
    public void process(Order order, OrderItem item) {
        boolean hasBasicAndEnterprise = order.getItems()
                .stream()
                .filter(orderItem -> orderItem.getProductType() == ProductType.SUBSCRIPTION)
                .map(OrderItem::getProductId)
                .anyMatch(productId -> productId.equals("SUB-BASIC-001"))
                &&
                order.getItems()
                        .stream()
                        .filter(orderItem -> orderItem.getProductType() == ProductType.SUBSCRIPTION)
                        .map(OrderItem::getProductId)
                        .anyMatch(productId -> productId.equals("SUB-ENTERPRISE-001"));

        if (hasBasicAndEnterprise) {
            throw new OrderProcessingException(
                    Failure.INCOMPATIBLE_SUBSCRIPTIONS,
                    "Customer cannot have Basic and Enterprise subscriptions in the same order"
            );
        }

        log.info(
                "Subscription item processed. orderId={}, productId={}",
                order.getId(),
                item.getProductId()
        );
    }
}