package com.pyxis.petstore.controller;

import com.pyxis.petstore.domain.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class OrderController {
    private final Cart cart;

    @Autowired
    public OrderController(Cart cart) {
        this.cart = cart;
    }

    @ModelAttribute("cart") 
    public Cart getCart() {
        return cart;
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    public String checkout() {
        return "order/new";
    }
}
