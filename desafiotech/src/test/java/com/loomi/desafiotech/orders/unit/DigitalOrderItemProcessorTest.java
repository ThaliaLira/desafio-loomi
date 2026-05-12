package com.loomi.desafiotech.orders.unit;

import com.loomi.desafiotech.orders.api.exception.OrderProcessingException;
import com.loomi.desafiotech.orders.application.processor.DigitalOrderItemProcessor;
import com.loomi.desafiotech.orders.domain.enums.ProductType;
import com.loomi.desafiotech.orders.domain.model.DigitalProductOwnership;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.OrderItem;
import com.loomi.desafiotech.orders.infrastructure.repository.DigitalProductOwnershipRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DigitalOrderItemProcessorTest {

    @Mock
    private DigitalProductOwnershipRepository ownershipRepository;

    @InjectMocks
    private DigitalOrderItemProcessor processor;

    @Test
    void shouldProcessDigitalProductAndCreateOwnership() {
        Order order = new Order("customer-digital-001");

        OrderItem item = new OrderItem(
                "EBOOK-JAVA-001",
                "Effective Java",
                ProductType.DIGITAL,
                1,
                new BigDecimal("39.90"),
                "{\"format\":\"PDF\"}"
        );

        when(ownershipRepository.existsByCustomerIdAndProductId(
                "customer-digital-001",
                "EBOOK-JAVA-001"
        )).thenReturn(false);

        processor.process(order, item);

        ArgumentCaptor<DigitalProductOwnership> ownershipCaptor =
                ArgumentCaptor.forClass(DigitalProductOwnership.class);

        verify(ownershipRepository).save(ownershipCaptor.capture());

        DigitalProductOwnership ownership = ownershipCaptor.getValue();

        assertThat(ownership).isNotNull();
    }

    @Test
    void shouldFailWhenCustomerAlreadyOwnsDigitalProduct() {
        Order order = new Order("customer-digital-001");

        OrderItem item = new OrderItem(
                "EBOOK-JAVA-001",
                "Effective Java",
                ProductType.DIGITAL,
                1,
                new BigDecimal("39.90"),
                "{\"format\":\"PDF\"}"
        );

        when(ownershipRepository.existsByCustomerIdAndProductId(
                "customer-digital-001",
                "EBOOK-JAVA-001"
        )).thenReturn(true);

        assertThatThrownBy(() -> processor.process(order, item))
                .isInstanceOf(OrderProcessingException.class)
                .hasMessageContaining("Digital product already owned");
    }
}