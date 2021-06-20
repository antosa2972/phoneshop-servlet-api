package com.es.phoneshop.model.product;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.order_service.DefaultOrderService;
import com.es.phoneshop.service.order_service.OrderService;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderServiceTest {
    OrderService orderService = DefaultOrderService.getInstance();
    Cart cart = new Cart();


    @Test
    public void  getOrderTest1(){
        assertNotNull(orderService.getOrder(cart));
    }
}
