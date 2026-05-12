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

import java.time.LocalDate;

@Component
public class PreOrderItemProcessor implements OrderItemProcessor {

    private static final Logger log =
            LoggerFactory.getLogger(PreOrderItemProcessor.class);

    private final ObjectMapper objectMapper;

    public PreOrderItemProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ProductType supports() {
        return ProductType.PRE_ORDER;
    }

    @Override
    public void process(Order order, OrderItem item) {

        try {

            JsonNode metadata =
                    objectMapper.readTree(item.getMetadata());

            String releaseDateText =
                    metadata.path("releaseDate").asText();

            LocalDate releaseDate =
                    LocalDate.parse(releaseDateText);

            if (!releaseDate.isAfter(LocalDate.now())) {

                throw new OrderProcessingException(
                        Failure.RELEASE_DATE_PASSED,
                        "Release date must be in the future"
                );
            }

            log.info(
                    "Pre-order processed. orderId={}, productId={}, releaseDate={}",
                    order.getId(),
                    item.getProductId(),
                    releaseDate
            );

        } catch (OrderProcessingException exception) {

            throw exception;

        } catch (Exception exception) {

            throw new OrderProcessingException(
                    Failure.INVALID_RELEASE_DATE,
                    "Invalid release date"
            );
        }
    }
}