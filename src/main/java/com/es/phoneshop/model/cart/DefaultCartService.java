package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultCartService implements CartService {

    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private final ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    private static class SingletonHelper {
        private static final DefaultCartService INSTANCE = new DefaultCartService();
    }

    public static DefaultCartService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
        }
        return cart;
    }

    private void exceptionCheck(Product product, int quantity) throws OutOfStockException {
        if (product.getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);
        exceptionCheck(product, quantity);
        AtomicBoolean isProductUpdated = new AtomicBoolean(false);
        if (cart.getItems().size() == 0) {
            cart.getItems().add(new CartItem(product, quantity));
        } else {
            cart.getItems().forEach(cartItem -> {
                if (cartItem.getProduct().getCode().equals(product.getCode())) {
                    int indexOfElementToUpdate = cart.getItems().indexOf(cartItem);
                    int oldQuantity = cart.getItems().get(indexOfElementToUpdate).getQuantity();
                    int newQuantity = oldQuantity + quantity;
                    try {
                        exceptionCheck(product, newQuantity);
                    } catch (OutOfStockException e) {
                        e.printStackTrace();
                    }
                    cart.getItems().set(indexOfElementToUpdate, new CartItem(product, newQuantity));
                    isProductUpdated.set(true);
                }
            });
            if (!isProductUpdated.get() && !cart.getItems().isEmpty()) {
                exceptionCheck(product, quantity);
                cart.getItems().add(new CartItem(product, quantity));
            }
        }
    }
}
