package com.loomi.desafiotech.orders.shared.exceptions;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String productId) {
        super("Product " + productId + " not found");
    }
}