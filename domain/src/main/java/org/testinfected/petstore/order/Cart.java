package org.testinfected.petstore.order;

import org.testinfected.petstore.product.Item;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cart implements Serializable {

    private final List<CartItem> cartItems = new ArrayList<>();

    public boolean empty() {
        return cartItems.isEmpty();
    }

    public BigDecimal getGrandTotal() {
        BigDecimal grandTotal = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            grandTotal = grandTotal.add(cartItem.getTotalPrice());
        }
        return grandTotal;
    }

    public int getTotalQuantity() {
        int total = 0;
        for (CartItem cartItem : cartItems) {
            total += cartItem.getQuantity();
        }
        return total;
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(cartItems);
    }

    public void add(Item item) {
        CartItem cartItem = findCartItemFor(item);
        if (cartItem != null) {
            cartItem.incrementQuantity();
        } else {
            cartItems.add(new CartItem(item));
        }
    }

    public void clear() {
        cartItems.clear();
    }

    private CartItem findCartItemFor(Item item) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.holds(item)) return cartItem;
        }
        return null;
    }
}
