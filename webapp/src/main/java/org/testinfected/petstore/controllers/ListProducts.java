package org.testinfected.petstore.controllers;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.petstore.Page;
import org.testinfected.petstore.product.AttachmentStorage;
import org.testinfected.petstore.product.Product;
import org.testinfected.petstore.product.ProductCatalog;
import org.testinfected.petstore.util.Context;

import java.io.IOException;
import java.io.Writer;
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

    private static class PathToAttachment implements Mustache.Lambda {

        public static PathToAttachment in(AttachmentStorage storage) {
            return new PathToAttachment(storage);
        }

        private final AttachmentStorage attachments;

        public PathToAttachment(AttachmentStorage attachments) {
            this.attachments = attachments;
        }

        public void execute(Template.Fragment frag, Writer out) throws IOException {
            out.write(attachments.getLocation(frag.execute()));
        }
    }
}
