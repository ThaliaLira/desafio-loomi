package com.loomi.desafiotech.orders.application.service;


import com.loomi.desafiotech.orders.domain.model.Product;
import com.loomi.desafiotech.orders.infrastructure.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductCatalogService {


    private final ProductRepository productRepository;

    public ProductCatalogService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getAvailableProductByProductId(String productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product " + productId + " not found"));

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new IllegalArgumentException("Product " + productId + " is not available");
        }

        return product;
    }
}
