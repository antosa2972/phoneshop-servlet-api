package com.es.phoneshop.model.order.exception;

public class OrderNotFoundException extends Exception {
    private final Long id;

    public OrderNotFoundException(Long productCode) {
        this.id = productCode;
    }

    public Long getId() {
        return id;
    }
}
