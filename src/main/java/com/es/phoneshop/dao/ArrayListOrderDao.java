package com.es.phoneshop.dao;

import com.es.phoneshop.model.order.Order;

import java.util.NoSuchElementException;

public class ArrayListOrderDao extends GenericDao<Order> implements OrderDao {
    private static OrderDao instance;

    public static synchronized OrderDao getInstance() {
        if (instance == null) {
            instance = new ArrayListOrderDao();
        }
        return instance;
    }

    @Override
    public Order getItemBySecureId(String secureId) {
        readWriteLock.readLock().lock();
        try {
            readWriteLock.readLock().lock();
            return items.stream()
                    .filter(item -> secureId.equals(item.getSecureId()))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException("No such order!"));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}
