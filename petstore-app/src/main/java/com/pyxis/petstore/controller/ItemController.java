package com.pyxis.petstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemRepository;

@Controller
@RequestMapping("/item")
public class ItemController {

	private final ItemRepository itemRepository;

	@Autowired
	public ItemController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView showSearchPage()
	{
		return new ModelAndView("search");
	}

	@RequestMapping(value="/searchResults", method = RequestMethod.POST)
	public ModelAndView doSearch(@RequestParam("query") String query)
	{
		List<Item> matchingItems = itemRepository.findItemsByQuery(query);
		ModelAndView modelAndView = new ModelAndView("searchResults");
		modelAndView.addObject("matchingItems", matchingItems);
		return modelAndView;
	}

}
