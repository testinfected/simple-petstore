package com.pyxis.petstore.controller;

import java.util.List;

import com.pyxis.petstore.domain.ItemInventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pyxis.petstore.domain.Item;

@Controller
public class ItemsController {

	private final ItemInventory itemInventory;

    @Autowired
	public ItemsController(ItemInventory itemInventory) {
		this.itemInventory = itemInventory;
	}
	
    @RequestMapping(method = RequestMethod.GET)
	public List<Item> index(@RequestParam("product_number") String productNumber) {
		return itemInventory.findByProductNumber(productNumber);
	}
}
