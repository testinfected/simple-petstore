package com.pyxis.petstore.domain.order;

import com.pyxis.petstore.domain.product.Item;

import java.math.BigDecimal;

public class CartItem {

    private final Item item;
    private final BigDecimal unitPrice;
    private int quantity;

    public CartItem(Item item) {
        this.item = item;
        this.quantity = 1;
        this.unitPrice = item.getPrice();
    }

    public BigDecimal getTotalPrice() {
        return getUnitPrice().multiply(new BigDecimal(quantity));
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public String getItemDescription() {
        return item.getDescription();
    }

    public String getItemNumber() {
        return item.getNumber();
    }

    public void incrementQuantity() {
        quantity++;
    }

    public boolean holds(Item item) {
        return this.item.equals(item);
    }
}
