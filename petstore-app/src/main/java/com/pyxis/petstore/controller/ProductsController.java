package com.pyxis.petstore.controller;

import com.pyxis.petstore.domain.AttachmentStorage;
import com.pyxis.petstore.domain.Product;
import com.pyxis.petstore.domain.ProductCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductsController {

	private final ProductCatalog productCatalog;
    private final AttachmentStorage attachmentStorage;

    @Autowired
    public ProductsController(ProductCatalog productCatalog, AttachmentStorage attachmentStorage) {
        this.productCatalog = productCatalog;
        this.attachmentStorage = attachmentStorage;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelMap index(@RequestParam("keyword") String keyword) {
        List<Product> matchingProducts = productCatalog.findByKeyword(keyword);
        ModelMap modelMap = model();
        modelMap.addAttribute("keyword", keyword);
        modelMap.addAttribute(matchingProducts);
        return modelMap;
    }

    private ModelMap model() {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("attachments", attachmentStorage);
        return modelMap;
    }
}
