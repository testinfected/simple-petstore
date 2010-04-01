package com.pyxis.petstore.controller;

import com.pyxis.petstore.domain.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class OrdersController {
    private final Cart cart;

    @Autowired
    public OrdersController(Cart cart) {
        this.cart = cart;
    }

    @InitBinder
    public void configureDataBinding(WebDataBinder binder) {
        // Here's how to configure field access. To set it globally configure the
        // AnnotationMethodHandlerAdapter to use a custom WebBindingInitializer
//        binder.initDirectFieldAccess();
    }

    @ModelAttribute("cart") 
    public Cart getCart() {
        return cart;
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    public String checkout() {
        return "orders/new";
    }

    @RequestMapping(method = RequestMethod.POST)
    public void create() {
    }
}
