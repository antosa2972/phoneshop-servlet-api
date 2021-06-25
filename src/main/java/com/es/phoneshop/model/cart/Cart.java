package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.cart.cart_item.CartItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private List<CartItem> items;
    private int totalQuantity;
    private BigDecimal totalCost;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public List<CartItem> getItems() {
        return this.items;
    }

    @Override
    public String toString() {
        return "Cart["
                + items +
                "]";
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
