package com.loomi.desafiotech.orders.domain.model;

import com.loomi.desafiotech.orders.domain.enums.ProductType;

import java.math.BigDecimal;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {

    @Id
    private UUID id;

    @Column(name = "product_id", nullable = false, unique = true, length = 50)
    private String productId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false, length = 30)
    private ProductType productType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Product() {
    }

    @PrePersist
    public void prePersist() {
    LocalDateTime now = LocalDateTime.now();

    if (id == null) {
    id = UUID.randomUUID();
    }

    createdAt = now;
    updatedAt = now;
    }

    public void decreaseStock(Integer quantity) {

        if (stockQuantity == null) {
            return;
        }

        this.stockQuantity -= quantity;
    }

    public void increaseStock(Integer quantity) {

        if (stockQuantity == null) {
            return;
        }

        this.stockQuantity += quantity;
    }

    @PreUpdate
    public void preUpdate() {
    updatedAt = LocalDateTime.now();
    }

    public String getProductId() {
    return productId;
    }

    public String getName() {
    return name;
    }

    public ProductType getProductType() {
    return productType;
    }

    public BigDecimal getPrice() {
    return price;
    }

    public Integer getStockQuantity() {
    return stockQuantity;
    }

    public Boolean getActive() {
    return active;
    }

}
