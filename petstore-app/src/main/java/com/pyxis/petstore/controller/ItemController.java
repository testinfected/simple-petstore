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

@Controller @RequestMapping("/item")
public class ItemController {

    private final ItemCatalog itemCatalog;

    @Autowired
    public ItemController(ItemCatalog itemCatalog) {
        this.itemCatalog = itemCatalog;
    }

    @RequestMapping(value = "/searchResults", method = RequestMethod.POST)
    public ModelAndView doSearch(@RequestParam("query") String query) {
        List<Item> matchingItems = itemCatalog.findItemsByKeyword(query);
        ModelAndView modelAndView = new ModelAndView("searchResults");
        modelAndView.addObject("matchingItems", matchingItems);
        return modelAndView;
    }

}
