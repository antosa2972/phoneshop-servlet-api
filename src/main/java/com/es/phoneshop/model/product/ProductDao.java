package com.es.phoneshop.model.product;

import com.es.phoneshop.model.enumsort.SortField;
import com.es.phoneshop.model.enumsort.SortOrder;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNotFoundException;
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);
    void save(Product product);
    void delete(Long id);
}
