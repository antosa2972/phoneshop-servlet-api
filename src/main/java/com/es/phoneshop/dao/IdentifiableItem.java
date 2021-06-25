package com.es.phoneshop.dao;

public abstract class IdentifiableItem {
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}