package com.pyxis.petstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemRepository;

@Controller @RequestMapping("/items")
public class ItemsController {

	private final ItemRepository itemRepository;

    @Autowired
	public ItemsController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}
	
    @RequestMapping(method = RequestMethod.GET)
	public ModelMap index(@RequestParam("product_number") String productNumber) {
		List<Item> items = itemRepository.findItemsByProductNumber(productNumber);
		return new ModelMap(items);
	}

}
