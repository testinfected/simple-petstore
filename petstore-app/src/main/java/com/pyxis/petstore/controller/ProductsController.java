package com.pyxis.petstore.controller;

import com.pyxis.petstore.domain.Product;
import com.pyxis.petstore.domain.ProductCatalog;
import com.pyxis.petstore.domain.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller @RequestMapping("/products")
public class ProductsController {

	private final ProductCatalog productCatalog;
    private final Storage storage;

    @Autowired
    public ProductsController(ProductCatalog productCatalog, Storage storage) {
        this.productCatalog = productCatalog;
        this.storage = storage;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelMap index(@RequestParam("keyword") String keyword) {
        List<Product> matchingProducts = productCatalog.findProductsByKeyword(keyword);
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("storage", storage);
        modelMap.addAttribute(matchingProducts);
        return modelMap;
    }

}
