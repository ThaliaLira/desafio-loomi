package com.loomi.desafiotech.orders.unit;

import com.loomi.desafiotech.orders.api.exception.OrderProcessingException;
import com.loomi.desafiotech.orders.application.processor.PhysicalOrderItemProcessor;
import com.loomi.desafiotech.orders.domain.enums.ProductType;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.OrderItem;
import com.loomi.desafiotech.orders.domain.model.Product;
import com.loomi.desafiotech.orders.infrastructure.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhysicalOrderItemProcessorTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private PhysicalOrderItemProcessor processor;

    @Test
    void shouldProcessPhysicalItemAndDecreaseStock() {
        Order order = new Order("customer-123");

        OrderItem item = new OrderItem(
                "BOOK-CC-001",
                "Clean Code",
                ProductType.PHYSICAL,
                2,
                new BigDecimal("89.90"),
                "{\"warehouseLocation\":\"SP\"}"
        );

        Product product = new Product(
                "BOOK-CC-001",
                "Clean Code",
                ProductType.PHYSICAL,
                new BigDecimal("89.90"),
                10,
                true,
                null
        );

        when(productRepository.findByProductId("BOOK-CC-001"))
                .thenReturn(Optional.of(product));

        processor.process(order, item);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(productCaptor.capture());

        Product savedProduct = productCaptor.getValue();

        assertThat(savedProduct.getStockQuantity()).isEqualTo(8);
    }

    @Test
    void shouldFailWhenStockIsInsufficient() {
        Order order = new Order("customer-123");

        OrderItem item = new OrderItem(
                "BOOK-CC-001",
                "Clean Code",
                ProductType.PHYSICAL,
                999,
                new BigDecimal("89.90"),
                "{\"warehouseLocation\":\"SP\"}"
        );

        Product product = new Product(
                "BOOK-CC-001",
                "Clean Code",
                ProductType.PHYSICAL,
                new BigDecimal("89.90"),
                10,
                true,
                null
        );

        when(productRepository.findByProductId("BOOK-CC-001"))
                .thenReturn(Optional.of(product));

        assertThatThrownBy(() -> processor.process(order, item))
                .isInstanceOf(OrderProcessingException.class)
                .hasMessageContaining("Insufficient stock");
    }
}