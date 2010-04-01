package com.pyxis.petstore.controller;

import com.pyxis.petstore.domain.Cart;
import com.pyxis.petstore.domain.CheckoutAssistant;
import com.pyxis.petstore.domain.CreditCardType;
import com.pyxis.petstore.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PurchasesController {
    private final Cart cart;
    private final CheckoutAssistant checkoutAssistant;

    @Autowired
    public PurchasesController(Cart cart, CheckoutAssistant checkoutAssistant) {
        this.cart = cart;
        this.checkoutAssistant = checkoutAssistant;
    }

    @InitBinder
    public void configureDataBinding(WebDataBinder binder) {
        // Here's how to configure field access. To set it globally configure the
        // AnnotationMethodHandlerAdapter to use a custom WebBindingInitializer
//        binder.initDirectFieldAccess();
    }

    @ModelAttribute("cardTypes")
    public CreditCardType[] getCreditCardTypes() {
        return CreditCardType.values();
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    public ModelAndView checkout() {
        Order order = checkoutAssistant.checkout(cart);
        return new ModelAndView("purchases/new").addObject(order);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void create() {
    }
}
