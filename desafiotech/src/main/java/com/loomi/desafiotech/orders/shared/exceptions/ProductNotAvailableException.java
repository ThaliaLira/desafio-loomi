package com.loomi.desafiotech.orders.shared.exceptions;

public class ProductNotAvailableException extends RuntimeException {

    public ProductNotAvailableException(String productId) {
        super("Product " + productId + " is not available");
    }
}