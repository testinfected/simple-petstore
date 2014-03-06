package org.testinfected.petstore.views;

import org.testinfected.petstore.order.Cart;

import java.util.HashMap;
import java.util.Map;

public class RenderedPage {

    private final Map<String, String> elements = new HashMap<String, String>();
    private Cart cart = new Cart();

    public RenderedPage withCart(Cart cart) {
        this.cart = cart;
        return this;
    }

    public boolean getCart() {
        return !cart.empty();
    }

    public int getCartQuantity() {
        return cart.getTotalQuantity();
    }

    public RenderedPage composedOf(Map<String, String> fragments) {
        this.elements.putAll(fragments);
        return this;
    }

    public String getSection() {
        return elements.get("section");
    }

    public String getBody() {
        return elements.get("body");
    }

    public String getHead() {
        return elements.get("head");
    }

    public String getTitle() {
        return elements.get("title");
    }
}
