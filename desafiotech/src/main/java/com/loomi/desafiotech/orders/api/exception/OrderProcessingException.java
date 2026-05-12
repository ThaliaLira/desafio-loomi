package com.loomi.desafiotech.orders.api.exception;

import com.loomi.desafiotech.orders.domain.enums.Failure;

public class OrderProcessingException extends RuntimeException {

    private final Failure failureReason;

    public OrderProcessingException(Failure failureReason, String message) {
        super(message);
        this.failureReason = failureReason;
    }

    public Failure getFailureReason() {
        return failureReason;
    }
}