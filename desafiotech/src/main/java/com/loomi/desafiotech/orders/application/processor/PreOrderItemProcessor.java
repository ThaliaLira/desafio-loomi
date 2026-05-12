package com.loomi.desafiotech.orders.application.processor;

import com.loomi.desafiotech.orders.domain.enums.ProductType;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PreOrderItemProcessor implements OrderItemProcessor {

    private static final Logger log = LoggerFactory.getLogger(PreOrderItemProcessor.class);

    @Override
    public ProductType supports() {
        return ProductType.PRE_ORDER;
    }

    @Override
    public void process(Order order, OrderItem item) {
        log.info(
                "Pre-order item processed. orderId={}, productId={}",
                order.getId(),
                item.getProductId()
        );
    }
}