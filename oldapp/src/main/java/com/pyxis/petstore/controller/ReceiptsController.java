package com.pyxis.petstore.controller;

import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderBook;
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
    private final OrderBook orderBook;

    @Autowired
    public ReceiptsController(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    @RequestMapping(value = "/{orderNumber}", method = RequestMethod.GET)
    public String show(@PathVariable String orderNumber, Model model) {
        Order order = orderBook.find(new OrderNumber(orderNumber));
        model.addAttribute("order", order);
        return "receipts/show";
    }
}
