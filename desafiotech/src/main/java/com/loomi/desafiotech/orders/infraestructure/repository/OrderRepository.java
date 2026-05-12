package com.loomi.desafiotech.orders.infraestructure.repository;

import com.loomi.desafiotech.orders.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByCustomerId(String customerId);
}