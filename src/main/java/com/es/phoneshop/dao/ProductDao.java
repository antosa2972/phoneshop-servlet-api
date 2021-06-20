package com.es.phoneshop.dao;

import com.es.phoneshop.model.enumsort.SortField;
import com.es.phoneshop.model.enumsort.SortOrder;
import com.es.phoneshop.model.product.Product;

import java.util.List;

public interface ProductDao {
    Product getItem(Long id);

    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    void save(Product product);

    void delete(Long id);
}
