package com.loomi.desafiotech.orders.application.processor;

import com.loomi.desafiotech.orders.domain.enums.ProductType;
import com.loomi.desafiotech.orders.domain.model.Order;
import com.loomi.desafiotech.orders.domain.model.OrderItem;

public interface OrderItemProcessor {

    ProductType supports();

    void process(Order order, OrderItem item);
}