package com.pyxis.petstore.domain;

import java.math.BigDecimal;

public class LineItem {
    private final String itemNumber;
    private final BigDecimal totalPrice;
    private final int quantity;

    public static LineItem from(CartItem cartItem) {
        return new LineItem(cartItem);
    }

    public LineItem(CartItem cartItem) {
        this.itemNumber = cartItem.getItemNumber();
        this.quantity = cartItem.getQuantity();
        this.totalPrice = cartItem.getTotalPrice();
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public int getQuantity() {
        return quantity;
    }
}
