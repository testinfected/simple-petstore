package org.testinfected.petstore.order;

import org.testinfected.petstore.product.Item;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItem implements Serializable {

    private final Item item;
    private final BigDecimal unitPrice;
    private int quantity;

    public CartItem(Item item) {
        this.item = item;
        this.quantity = 1;
        this.unitPrice = item.price();
    }

    public BigDecimal totalPrice() {
        return unitPrice().multiply(new BigDecimal(quantity));
    }

    public int quantity() {
        return quantity;
    }

    public BigDecimal unitPrice() {
        return unitPrice;
    }

    public String itemDescription() {
        return item.description();
    }

    public String itemNumber() {
        return item.number();
    }

    public void incrementQuantity() {
        quantity++;
    }

    public boolean holds(Item item) {
        return this.item.equals(item);
    }

    public String toString() {
        return item.toString() + " (" + unitPrice + "$ x " + quantity + ")";
    }
}
