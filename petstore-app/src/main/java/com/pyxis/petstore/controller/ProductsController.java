package com.pyxis.petstore.controller;

import com.pyxis.petstore.domain.Product;
import com.pyxis.petstore.domain.ProductCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller @RequestMapping("/products")
public class ProductsController {

	public static final String SEARCH_RESULTS_VIEW = "searchResults";
    public static final String MATCHING_PRODUCTS_KEY = "matchingProducts";
    
	private final ProductCatalog productCatalog;

    @Autowired
    public ProductsController(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(@RequestParam("keyword") String keyword) {
        List<Product> matchingProducts = productCatalog.findProductsByKeyword(keyword);
        ModelAndView modelAndView = new ModelAndView(SEARCH_RESULTS_VIEW);
        modelAndView.addObject(MATCHING_PRODUCTS_KEY, matchingProducts);
        return modelAndView;
    }

}
