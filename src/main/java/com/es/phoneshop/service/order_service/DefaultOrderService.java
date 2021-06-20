package com.es.phoneshop.service.order_service;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.cart_item.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.exception.OrderNotFoundException;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {

    OrderDao orderDao = ArrayListOrderDao.getInstance();

    private static class SingletonHelper {
        private static final DefaultOrderService INSTANCE = new DefaultOrderService();
    }

    public static DefaultOrderService getInstance() {
        return DefaultOrderService.SingletonHelper.INSTANCE;
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems().stream().map(cartItem -> {
            try {
                return (CartItem) cartItem.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
        order.setSubtotal(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalCost(order.getSubtotal().add(order.getDeliveryCost()));
        return order;
    }

    @Override
    public void placeOrder(Order order) throws OrderNotFoundException {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    private BigDecimal calculateDeliveryCost(){
        Random random = new Random();
        int upperBound = 50;
        int deliveryCost = random.nextInt(upperBound);
        return new BigDecimal(deliveryCost);
    }
}
