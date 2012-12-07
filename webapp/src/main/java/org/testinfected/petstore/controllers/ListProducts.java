package org.testinfected.petstore.controllers;

import com.github.mustachejava.TemplateFunction;
import org.testinfected.petstore.Controller;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.product.AttachmentStorage;
import org.testinfected.petstore.product.Product;
import org.testinfected.petstore.product.ProductCatalog;
import org.testinfected.petstore.util.Context;
import org.testinfected.support.Request;
import org.testinfected.support.Response;

import java.util.List;

import static org.testinfected.petstore.util.Context.context;

public class ListProducts implements Controller {

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
                and("photo", LocateAttachment.in(attachmentStorage));
        productsPage.render(response, context.asMap());
    }

    private static class LocateAttachment implements TemplateFunction {
        public static LocateAttachment in(AttachmentStorage storage) {
            return new LocateAttachment(storage);
        }

        private final AttachmentStorage attachments;

        public LocateAttachment(AttachmentStorage attachments) {
            this.attachments = attachments;
        }

        public String apply(String fileName) {
            return attachments.getLocation(fileName);
        }
    }
}
