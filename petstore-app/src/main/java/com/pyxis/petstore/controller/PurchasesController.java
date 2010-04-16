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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@Controller
@SessionAttributes("paymentDetails")
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

    public CreditCardType[] availableCardTypes() {
        return CreditCardType.values();
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    public String checkout(Model model) {
        model.addAttribute(cart);
        model.addAttribute("cardTypes", availableCardTypes());
        model.addAttribute("paymentDetails", new CreditCardDetails());
        return "purchases/new";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("paymentDetails") CreditCardDetails paymentDetails, BindingResult form, SessionStatus status, Model model) {
        if (form.hasErrors()) {
            form.reject("invalid");
            model.addAttribute(cart);
            model.addAttribute("cardTypes", availableCardTypes());
            return "purchases/new";
        }
        status.setComplete();
        Order order = checkoutAssistant.checkout(cart);
        paymentCollector.collectPayment(order, paymentDetails);
        return "redirect:/receipts/" + order.getNumber();
    }
}
