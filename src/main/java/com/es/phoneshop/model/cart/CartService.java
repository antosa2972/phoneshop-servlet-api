package com.es.phoneshop.model.cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest httpServletRequest);
    void add(Cart cart, Long productId,int quantity) throws OutOfStockException;
}
