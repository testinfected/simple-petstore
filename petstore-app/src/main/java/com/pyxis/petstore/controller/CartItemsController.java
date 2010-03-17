package com.pyxis.petstore.controller;

import com.pyxis.petstore.domain.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CartItemsController {

    @Autowired
    public CartItemsController(Cart cart) {
    }
}
