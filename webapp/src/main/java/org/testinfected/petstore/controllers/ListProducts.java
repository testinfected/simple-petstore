package org.testinfected.petstore.controllers;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import org.testinfected.petstore.View;
import org.testinfected.petstore.product.AttachmentStorage;
import org.testinfected.petstore.product.Product;
import org.testinfected.petstore.product.ProductCatalog;
import org.testinfected.petstore.views.Products;

import java.util.List;

public class ListProducts implements Application {

    private final ProductCatalog productCatalog;
    private final AttachmentStorage attachmentStorage;
    private final View<Products> view;

    public ListProducts(ProductCatalog productCatalog, AttachmentStorage attachmentStorage, View<Products> view) {
        this.productCatalog = productCatalog;
        this.attachmentStorage = attachmentStorage;
        this.view = view;
    }

    public void handle(Request request, Response response) throws Exception {
        String keyword = request.parameter("keyword");
        List<Product> found = productCatalog.findByKeyword(keyword);
        view.render(response, new Products().matching(keyword)
                                                    .add(found)
                                                    .withPhotosIn(attachmentStorage));
    }
}