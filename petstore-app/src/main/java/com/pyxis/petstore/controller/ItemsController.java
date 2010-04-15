package com.pyxis.petstore.controller;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping(value = "/products/{productNumber}")
public class ItemsController {

	private final ItemInventory itemInventory;

    @Autowired
	public ItemsController(ItemInventory itemInventory) {
		this.itemInventory = itemInventory;
	}

    @RequestMapping( value = "/items", method = RequestMethod.GET)
	public String index(@PathVariable("productNumber") String productNumber, Model model) {
        List<Item> items = itemInventory.findByProductNumber(productNumber);
        model.addAttribute(items);
        return "items";
    }
}
