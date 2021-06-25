package com.es.phoneshop.model.product.exception;

public class ProductNotFoundException extends RuntimeException {
    private final Long id;

    public ProductNotFoundException(Long productCode) {
        this.id = productCode;
    }

    public Long getId() {
        return id;
    }
}
