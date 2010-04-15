package com.pyxis.petstore.controller;

import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderLog;
import com.pyxis.petstore.domain.order.OrderNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/receipts")
public class ReceiptsController {
    private final OrderLog orderLog;

    @Autowired
    public ReceiptsController(OrderLog orderLog) {
        this.orderLog = orderLog;
    }

    @RequestMapping(value = "/{orderNumber}", method = RequestMethod.GET)
    public String show(@PathVariable String orderNumber, Model model) {
        Order order = orderLog.find(new OrderNumber(orderNumber));
        model.addAttribute(order);
        return "receipts/show";
    }
}
