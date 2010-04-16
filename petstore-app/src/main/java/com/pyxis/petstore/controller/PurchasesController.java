package com.pyxis.petstore.controller;

import com.pyxis.petstore.domain.billing.CreditCardDetails;
import com.pyxis.petstore.domain.billing.CreditCardType;
import com.pyxis.petstore.domain.order.Cart;
import com.pyxis.petstore.domain.order.CheckoutAssistant;
import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.PaymentCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PurchasesController {
    private final Cart cart;
    private final CheckoutAssistant checkoutAssistant;
    private final PaymentCollector paymentCollector;

    @Autowired
    public PurchasesController(Cart cart, CheckoutAssistant checkoutAssistant, PaymentCollector paymentCollector) {
        this.cart = cart;
        this.checkoutAssistant = checkoutAssistant;
        this.paymentCollector = paymentCollector;
    }

    @InitBinder
    public void configureDataBinding(WebDataBinder binder) {
        // todo Convert expiry date to Date or find how to do it automatically 
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    public String checkout(Model model) {
        model.addAttribute(cart);
        model.addAttribute("cardTypes", availableCardTypes());
        return "purchases/new";
    }

    private CreditCardType[] availableCardTypes() {
        return CreditCardType.values();
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(CreditCardDetails paymentDetails, BindingResult form) {
        Order order = checkoutAssistant.checkout(cart);
        paymentCollector.collectPayment(order, paymentDetails);
        return "redirect:/receipts/" + order.getNumber();
    }
}
