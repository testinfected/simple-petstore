package com.pyxis.petstore.controller;

import com.pyxis.petstore.domain.Item;
import com.pyxis.petstore.domain.ItemCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller @RequestMapping("/items")
public class ItemsController {

	public static final String SEARCH_RESULTS_VIEW_NAME = "searchResults";
    public static final String MATCHING_ITEMS_KEY = "matchingItems";
    
	private final ItemCatalog itemCatalog;

    @Autowired
    public ItemsController(ItemCatalog itemCatalog) {
        this.itemCatalog = itemCatalog;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView doSearch(@RequestParam("keyword") String keyword) {
        List<Item> matchingItems = itemCatalog.findItemsByKeyword(keyword);
        ModelAndView modelAndView = new ModelAndView(SEARCH_RESULTS_VIEW_NAME);
        modelAndView.addObject(MATCHING_ITEMS_KEY, matchingItems);
        return modelAndView;
    }

}
