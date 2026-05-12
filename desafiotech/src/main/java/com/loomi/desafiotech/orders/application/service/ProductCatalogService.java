package com.loomi.desafiotech.orders.application.service;


import com.loomi.desafiotech.orders.domain.model.Product;
import com.loomi.desafiotech.orders.infrastructure.repository.ProductRepository;
import com.loomi.desafiotech.orders.shared.exceptions.ProductNotAvailableException;
import com.loomi.desafiotech.orders.shared.exceptions.ProductNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProductCatalogService {


    private final ProductRepository productRepository;

    public ProductCatalogService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getAvailableProductByProductId(String productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (!Boolean.TRUE.equals(product.getActive())) {
            throw new ProductNotAvailableException(productId);
        }

        return product;
    }
}
