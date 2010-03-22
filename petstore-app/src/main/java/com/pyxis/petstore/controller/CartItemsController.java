package com.pyxis.petstore.controller;

import com.pyxis.petstore.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartItemsController {
    private final Basket basket;
    private final ItemInventory itemInventory;

    @Autowired
    public CartItemsController(Basket basket, ItemInventory itemInventory) {
        this.basket = basket;
        this.itemInventory = itemInventory;
    }

    @ModelAttribute("cart") @RequestMapping(method = RequestMethod.GET)
    public Basket index() {
        return basket;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestParam("item_number") String number) {
        Item item = itemInventory.find(new ItemNumber(number));
        basket.add(item);
        return "redirect:cartitems";
    }
}
