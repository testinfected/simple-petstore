package com.pyxis.petstore.domain;

public interface CheckoutAssistant {

    Order checkout(Cart cart);
}
