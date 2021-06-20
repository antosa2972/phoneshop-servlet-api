package com.es.phoneshop.model.product;

import com.es.phoneshop.dao.ArrayListOrderDao;
import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.exception.OrderNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.Assert.*;

public class ArrayListOrderDaoTest {

    private final OrderDao orderDao = ArrayListOrderDao.getInstance();
    private final Order order = new Order();
    private final Order newOrder = new Order();
    private final ProductDao productDao = ArrayListProductDao.getInstance();

    @Before
    public void setup() {
        order.setSecureId(UUID.randomUUID().toString());
        newOrder.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }

    @Test
    public void getOrderByIdTest() {
        Order orderFromDao = orderDao.getItem(order.getId());
        assertEquals(orderFromDao.getId(), order.getId());
    }
    @Test(expected = NoSuchElementException.class)
    public void deleteProductTest() {
        productDao.delete(1L);
        productDao.getItem(1L);
    }
    @Test
    public void getOrderBySecureIdTest() {
        orderDao.save(order);
        Order orderToCheck = orderDao.getItemBySecureId(order.getSecureId());
        assertEquals(orderToCheck.getSecureId(), order.getSecureId());
    }
    @Test
    public void saveExistingProductTest() {
        BigDecimal totalCost = order.getTotalCost();
        Long orderId = order.getId();
        order.setTotalCost(BigDecimal.valueOf(150L));
        orderDao.save(order);
        assertNotEquals(orderDao.getItem(orderId).getTotalCost(), totalCost);
    }
    @Test
    public void saveNewOrderTest() {
        String newOrderSecureId = newOrder.getSecureId();
        orderDao.save(newOrder);
        assertEquals(orderDao.getItemBySecureId(newOrderSecureId), newOrder);
    }
    @Test(expected = NullPointerException.class)
    public void saveNewOrder1() {
        String newOrderSecureId = newOrder.getSecureId();
        orderDao.save(null);
        assertNull(orderDao.getItemBySecureId(newOrderSecureId));
    }
}