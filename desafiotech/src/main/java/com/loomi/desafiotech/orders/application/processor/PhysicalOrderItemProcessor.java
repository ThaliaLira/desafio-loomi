package com.loomi.desafiotech.orders.application.processor;

import com.loomi.desafiotech.orders.api.exception.OrderProcessingException;
import com.loomi.desafiotech.orders.domain.enums.Failure;
import com.loomi.desafiotech.orders.domain.enums.ProductType;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.OrderItem;
import com.loomi.desafiotech.orders.domain.model.Product;
import com.loomi.desafiotech.orders.infrastructure.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PhysicalOrderItemProcessor implements OrderItemProcessor {

    private static final Logger log = LoggerFactory.getLogger(PhysicalOrderItemProcessor.class);

    private final ProductRepository productRepository;

    public PhysicalOrderItemProcessor(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductType supports() {
        return ProductType.PHYSICAL;
    }

    @Override
    public void process(Order order, OrderItem item) {
        Product product = productRepository.findByProductId(item.getProductId())
                .orElseThrow(() -> new OrderProcessingException(
                        Failure.PRODUCT_NOT_AVAILABLE,
                        "Product " + item.getProductId() + " not found during physical processing"
                ));

        Integer stockQuantity = product.getStockQuantity();

        if (stockQuantity == null || stockQuantity < item.getQuantity()) {
            throw new OrderProcessingException(
                    Failure.OUT_OF_STOCK,
                    "Insufficient stock for product " + item.getProductId()
            );
        }

        if (stockQuantity < 5) {
            log.warn(
                    "Low stock alert. productId={}, currentStock={}",
                    item.getProductId(),
                    stockQuantity
            );
        }

        log.info(
                "Physical item processed. orderId={}, productId={}, quantity={}",
                order.getId(),
                item.getProductId(),
                item.getQuantity()
        );
    }
}