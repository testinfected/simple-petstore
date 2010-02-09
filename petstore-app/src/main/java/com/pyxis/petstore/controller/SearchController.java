package com.pyxis.petstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/search")
public class SearchController {

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showSearchPage()
	{
		return new ModelAndView("search");
	}

	@RequestMapping(value="/do", method = RequestMethod.POST)
	public ModelAndView doSearch()
	{
		return new ModelAndView("home");
	}

}
