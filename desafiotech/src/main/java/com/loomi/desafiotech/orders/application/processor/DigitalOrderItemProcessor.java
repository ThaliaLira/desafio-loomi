package com.loomi.desafiotech.orders.application.processor;

import com.loomi.desafiotech.orders.domain.enums.ProductType;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DigitalOrderItemProcessor implements OrderItemProcessor {

    private static final Logger log = LoggerFactory.getLogger(DigitalOrderItemProcessor.class);

    @Override
    public ProductType supports() {
        return ProductType.DIGITAL;
    }

    @Override
    public void process(Order order, OrderItem item) {
        String licenseKey = UUID.randomUUID().toString();

        log.info(
                "Digital item processed. orderId={}, productId={}, licenseKey={}",
                order.getId(),
                item.getProductId(),
                licenseKey
        );
    }
}