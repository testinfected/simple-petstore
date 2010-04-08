package com.pyxis.petstore.controller;

import com.pyxis.petstore.domain.product.AttachmentStorage;
import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class ProductsController {

	private final ProductCatalog productCatalog;
    private final AttachmentStorage attachmentStorage;

    @Autowired
    public ProductsController(ProductCatalog productCatalog, AttachmentStorage attachmentStorage) {
        this.productCatalog = productCatalog;
        this.attachmentStorage = attachmentStorage;
    }

    @ModelAttribute("attachments")
    public AttachmentStorage getAttachmentStorage() {
        return attachmentStorage;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, ?> index(@RequestParam("keyword") String keyword) {
        List<Product> matchingProducts = productCatalog.findByKeyword(keyword);
        return new ModelMap().
                addAttribute("keyword", keyword).
                addAttribute(matchingProducts);
    }
}
