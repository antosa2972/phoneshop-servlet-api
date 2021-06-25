package com.es.phoneshop.service.orderservice;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.exception.OrderNotFoundException;

public interface OrderService {
    Order getOrder(Cart cart);

    void placeOrder(Order order) throws OrderNotFoundException;
}
