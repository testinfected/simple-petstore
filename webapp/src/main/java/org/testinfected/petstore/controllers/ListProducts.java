package org.testinfected.petstore.controllers;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.product.AttachmentStorage;
import org.testinfected.petstore.product.Product;
import org.testinfected.petstore.product.ProductCatalog;
import org.testinfected.petstore.util.Context;
import org.testinfected.petstore.views.PathToAttachment;

import java.util.List;

import static org.testinfected.petstore.util.Context.context;

public class ListProducts implements Application {

    private final ProductCatalog productCatalog;
    private final AttachmentStorage attachmentStorage;
    private final Page productsPage;

    public ListProducts(ProductCatalog productCatalog, AttachmentStorage attachmentStorage, Page productsPage) {
        this.productCatalog = productCatalog;
        this.attachmentStorage = attachmentStorage;
        this.productsPage = productsPage;
    }

    public void handle(Request request, Response response) throws Exception {
        String keyword = request.parameter("keyword");
        List<Product> products = productCatalog.findByKeyword(keyword);

        Context context = context().
                with("match-found", !products.isEmpty()).
                and("keyword", keyword).
                and("products", products).
                and("match-count", products.size()).
                and("path", PathToAttachment.in(attachmentStorage));
        productsPage.render(response, context.asMap());
    }
}
