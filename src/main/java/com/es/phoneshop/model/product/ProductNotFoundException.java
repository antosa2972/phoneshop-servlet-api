package com.es.phoneshop.model.product;

public class ProductNotFoundException extends RuntimeException {
    private final Long id;

    ProductNotFoundException(Long productCode) {
        this.id = productCode;
    }

    public Long getId() {
        return id;
    }
}
