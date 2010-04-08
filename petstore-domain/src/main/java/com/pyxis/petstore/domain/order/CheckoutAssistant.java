package com.pyxis.petstore.domain.order;

public interface CheckoutAssistant {

    Order checkout(Cart cart);
}
