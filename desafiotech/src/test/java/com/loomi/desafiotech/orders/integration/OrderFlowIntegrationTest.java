package com.loomi.desafiotech.orders.integration;

import org.junit.jupiter.api.Disabled;
import com.loomi.desafiotech.orders.domain.enums.OrderStatus;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.infrastructure.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Requires Docker/Testcontainers running locally")
public class OrderFlowIntegrationTest extends AbstractIntegrationTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldCreateAndProcessOrderSuccessfully() throws Exception {

        String requestBody = """
                {
                  "customerId": "customer-test-001",
                  "items": [
                    {
                      "productId": "BOOK-CC-001",
                      "quantity": 1,
                      "metadata": {
                        "warehouseLocation": "SP"
                      }
                    }
                  ]
                }
                """;

        String response = restTemplate.postForObject(
                "/api/orders",
                requestBody,
                String.class
        );

        assertThat(response).isNotNull();

        Thread.sleep(5000);

        Order order = orderRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        assertThat(order.getStatus())
                .isEqualTo(OrderStatus.PROCESSED);
    }

    @Test
    void shouldFailWhenProductIsOutOfStock() throws Exception {

        String requestBody = """
            {
              "customerId": "customer-test-002",
              "items": [
                {
                  "productId": "BOOK-CC-001",
                  "quantity": 9999,
                  "metadata": {
                    "warehouseLocation": "SP"
                  }
                }
              ]
            }
            """;

        restTemplate.postForObject(
                "/api/orders",
                requestBody,
                String.class
        );

        Thread.sleep(5000);

        Order order = orderRepository.findAll()
                .stream()
                .filter(o -> o.getCustomerId().equals("customer-test-002"))
                .findFirst()
                .orElseThrow();

        assertThat(order.getStatus())
                .isEqualTo(OrderStatus.FAILED);
    }
    @Test
    void shouldIgnoreDuplicatedEvents() throws Exception {

        String requestBody = """
            {
              "customerId": "customer-idempotent-001",
              "items": [
                {
                  "productId": "BOOK-CC-001",
                  "quantity": 1,
                  "metadata": {
                    "warehouseLocation": "SP"
                  }
                }
              ]
            }
            """;

        restTemplate.postForObject(
                "/api/orders",
                requestBody,
                String.class
        );

        Thread.sleep(5000);

        long processedEvents =
                orderRepository.findAll().size();

        Thread.sleep(3000);

        long processedEventsAgain =
                orderRepository.findAll().size();

        assertThat(processedEventsAgain)
                .isEqualTo(processedEvents);
    }

}