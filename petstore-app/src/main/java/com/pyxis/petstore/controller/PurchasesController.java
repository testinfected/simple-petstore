package com.pyxis.petstore.controller;

import com.pyxis.petstore.domain.billing.CreditCard;
import com.pyxis.petstore.domain.order.Cart;
import com.pyxis.petstore.domain.order.CheckoutAssistant;
import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.PaymentCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("order")
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
        // Here's how to configure field access. To set it globally configure the
        // AnnotationMethodHandlerAdapter to use a custom WebBindingInitializer
//        binder.initDirectFieldAccess();
        // todo Convert credit card type to CreditCard.Type
        // todo Convert expiration date to Date
        
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    public ModelAndView checkout() {
        Order order = checkoutAssistant.checkout(cart);
        return new ModelAndView("purchases/new").addObject(order).addObject("cardTypes", availableCardTypes());
    }

    private CreditCard.Type[] availableCardTypes() {
        return CreditCard.Type.values();
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(Order order, SessionStatus status, PaymentDetails paymentDetails, BindingResult form) {
        paymentCollector.collectPayment(order, paymentDetails.getCreditCard(), paymentDetails.getBillingAccount());
        status.setComplete();
        return "redirect:/receipts/" + order.getNumber();
    }
}
