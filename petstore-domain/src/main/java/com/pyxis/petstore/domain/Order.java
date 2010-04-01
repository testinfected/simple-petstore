package com.pyxis.petstore.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {
    private final List<LineItem> items = new ArrayList<LineItem>();

    public void addItemsFromCart(Cart cart) {
        for (CartItem cartItem : cart.getItems()) {
            items.add(LineItem.from(cartItem));
        }
    }

    public BigDecimal getTotalPrice() {
        BigDecimal total = BigDecimal.ZERO;
        for (LineItem lineItem : items) {
            total = total.add(lineItem.getTotalPrice());
        }
        return total;
    }

    public List<LineItem> getLineItems() {
        return Collections.unmodifiableList(items);
    }
}
