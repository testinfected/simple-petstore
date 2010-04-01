package com.pyxis.petstore.domain;

public class Cashier implements CheckoutAssistant {

    public Order checkout(Cart cart) {
        Order order = new Order();
        order.addItemsFromCart(cart);
        return order;
    }
}
