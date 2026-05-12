package com.loomi.desafiotech.orders.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "digital_product_ownership")
public class DigitalProductOwnership {

    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "license_key", nullable = false)
    private String licenseKey;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected DigitalProductOwnership() {
    }

    public DigitalProductOwnership(
            String customerId,
            String productId,
            String licenseKey
    ) {
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.productId = productId;
        this.licenseKey = licenseKey;
        this.createdAt = LocalDateTime.now();
    }
}