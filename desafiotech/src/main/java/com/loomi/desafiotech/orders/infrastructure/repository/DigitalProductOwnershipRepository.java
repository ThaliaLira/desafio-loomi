package com.loomi.desafiotech.orders.infrastructure.repository;

import com.loomi.desafiotech.orders.domain.model.DigitalProductOwnership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DigitalProductOwnershipRepository
        extends JpaRepository<DigitalProductOwnership, UUID> {

    boolean existsByCustomerIdAndProductId(
            String customerId,
            String productId
    );
}