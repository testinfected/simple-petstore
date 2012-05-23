package com.pyxis.petstore.controller;

import com.pyxis.petstore.Maybe;
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
        Maybe<Order> entry = orderBook.find(new OrderNumber(orderNumber));
        if (entry.exists()) model.addAttribute(entry.bare());
        return "receipts/show";
    }
}
