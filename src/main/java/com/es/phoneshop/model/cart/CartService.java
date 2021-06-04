package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public interface CartService {
    Cart getCart(HttpServletRequest httpServletRequest);
    void add(Cart cart, Long productId,int quantity) throws OutOfStockException;
    Set<Product> getRecentlyViewedProducts();
}
