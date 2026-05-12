package com.loomi.desafiotech.orders.application.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loomi.desafiotech.orders.api.exception.OrderProcessingException;
import com.loomi.desafiotech.orders.domain.enums.Failure;
import com.loomi.desafiotech.orders.domain.enums.ProductType;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CorporateOrderItemProcessor implements OrderItemProcessor {

    private static final Logger log =
            LoggerFactory.getLogger(CorporateOrderItemProcessor.class);

    private static final BigDecimal APPROVAL_THRESHOLD =
            new BigDecimal("50000");

    private final ObjectMapper objectMapper;

    public CorporateOrderItemProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ProductType supports() {
        return ProductType.CORPORATE;
    }

    @Override
    public void process(Order order, OrderItem item) {

        try {

            JsonNode metadata =
                    objectMapper.readTree(item.getMetadata());

            String cnpj =
                    metadata.path("cnpj").asText();

            if (cnpj.isBlank()) {

                throw new OrderProcessingException(
                        Failure.INVALID_CORPORATE_DATA,
                        "Invalid corporate data"
                );
            }

            if (order.getTotalAmount()
                    .compareTo(APPROVAL_THRESHOLD) > 0) {

                order.markAsPendingApproval();

                log.info(
                        "Corporate order pending approval. orderId={}, total={}",
                        order.getId(),
                        order.getTotalAmount()
                );

                return;
            }

            log.info(
                    "Corporate order processed. orderId={}, productId={}",
                    order.getId(),
                    item.getProductId()
            );

        } catch (OrderProcessingException exception) {

            throw exception;

        } catch (Exception exception) {

            throw new OrderProcessingException(
                    Failure.INVALID_CORPORATE_DATA,
                    "Invalid corporate metadata"
            );
        }
    }
}