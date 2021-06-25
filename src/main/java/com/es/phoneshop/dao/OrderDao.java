package com.es.phoneshop.dao;

import com.es.phoneshop.model.order.Order;

public interface OrderDao {
    Order getItem(Long id);

    Order getItemBySecureId(String secureId);

    void save(Order order);
}
