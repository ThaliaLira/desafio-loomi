package com.loomi.desafiotech.orders.infrastructure.repository;


import com.loomi.desafiotech.orders.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByProductId(String productId);
}

