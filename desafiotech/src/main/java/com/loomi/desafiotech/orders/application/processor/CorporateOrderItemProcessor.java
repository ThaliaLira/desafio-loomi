package com.loomi.desafiotech.orders.application.processor;

import com.loomi.desafiotech.orders.domain.enums.ProductType;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CorporateOrderItemProcessor implements OrderItemProcessor {

    private static final Logger log = LoggerFactory.getLogger(CorporateOrderItemProcessor.class);

    private static final BigDecimal MANUAL_APPROVAL_THRESHOLD = new BigDecimal("50000.00");

    @Override
    public ProductType supports() {
        return ProductType.CORPORATE;
    }

    @Override
    public void process(Order order, OrderItem item) {
        if (order.getTotalAmount().compareTo(MANUAL_APPROVAL_THRESHOLD) > 0) {
            order.markAsPendingApproval();

            log.info(
                    "Corporate order requires manual approval. orderId={}, totalAmount={}",
                    order.getId(),
                    order.getTotalAmount()
            );

            return;
        }

        log.info(
                "Corporate item processed. orderId={}, productId={}",
                order.getId(),
                item.getProductId()
        );
    }
}