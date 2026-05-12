package com.loomi.desafiotech.orders.application.processor;

import com.loomi.desafiotech.orders.api.exception.OrderProcessingException;
import com.loomi.desafiotech.orders.domain.enums.Failure;
import com.loomi.desafiotech.orders.domain.enums.ProductType;
import com.loomi.desafiotech.orders.domain.model.DigitalProductOwnership;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.OrderItem;
import com.loomi.desafiotech.orders.infrastructure.repository.DigitalProductOwnershipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DigitalOrderItemProcessor implements OrderItemProcessor {

    private static final Logger log =
            LoggerFactory.getLogger(DigitalOrderItemProcessor.class);

    private final DigitalProductOwnershipRepository ownershipRepository;

    public DigitalOrderItemProcessor(
            DigitalProductOwnershipRepository ownershipRepository
    ) {
        this.ownershipRepository = ownershipRepository;
    }

    @Override
    public ProductType supports() {
        return ProductType.DIGITAL;
    }

    @Override
    public void process(Order order, OrderItem item) {

        boolean alreadyOwned =
                ownershipRepository.existsByCustomerIdAndProductId(
                        order.getCustomerId(),
                        item.getProductId()
                );

        if (alreadyOwned) {

            throw new OrderProcessingException(
                    Failure.ALREADY_OWNED,
                    "Digital product already owned"
            );
        }

        String licenseKey = UUID.randomUUID().toString();

        ownershipRepository.save(
                new DigitalProductOwnership(
                        order.getCustomerId(),
                        item.getProductId(),
                        licenseKey
                )
        );

        log.info(
                "Digital product processed. orderId={}, productId={}, licenseKey={}",
                order.getId(),
                item.getProductId(),
                licenseKey
        );
    }
}