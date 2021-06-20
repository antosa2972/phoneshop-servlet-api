package com.es.phoneshop.service.cart_service;

import com.es.phoneshop.dao.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.cart_item.CartItem;
import com.es.phoneshop.model.cart.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
        Product product = productDao.getItem(productId);
        exceptionCheck(product, quantity);
        AtomicBoolean isProductUpdated = new AtomicBoolean(false);
        if (cart.getItems().size() == 0) {
            cart.getItems().add(new CartItem(product, quantity));
            product.setStock(product.getStock() - quantity);
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
                    product.setStock(product.getStock() - quantity);
                    isProductUpdated.set(true);
                }
            });
            if (!isProductUpdated.get() && !cart.getItems().isEmpty()) {
                exceptionCheck(product, quantity);
                cart.getItems().add(new CartItem(product, quantity));
                product.setStock(product.getStock() - quantity);
            }
        }
        recalculateCart(cart);
    }

    @Override
    public synchronized void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getItem(productId);
        if (quantity <= 0) {
            throw new OutOfStockException(product, quantity, 0);
        }
        if (productDao.getItem(productId).getStock() < quantity) {
            throw new OutOfStockException(product, quantity, product.getStock());
        }
        cart.getItems().forEach(cartItem -> {
            if (cartItem.getProduct().getCode().equals(product.getCode())) {
                cartItem.setQuantity(quantity);
            }
        });
        recalculateCart(cart);
    }

    @Override
    public synchronized void delete(Cart cart, Long productId) {
        cart.getItems().removeIf(cartItem -> productId.equals(cartItem.getProduct().getId())
        );
        recalculateCart(cart);
    }

    @Override
    public void clearCart(Cart cart) {
        cart.getItems().clear();
        cart.setTotalQuantity(0);
        cart.setTotalCost(new BigDecimal(0L));
    }

    public void recalculateCart(Cart cart) {
        BigDecimal totalPrice = new BigDecimal("0.0");
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity)
                .collect(Collectors.summingInt(q -> q.intValue())));
        for (CartItem cartItem : cart.getItems()) {
            totalPrice = totalPrice.add(cartItem.getProduct().getPrice());
        }
        cart.setTotalCost(totalPrice);
    }
}
