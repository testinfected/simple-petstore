package org.testinfected.petstore.endpoints;

import com.pyxis.petstore.domain.product.AttachmentStorage;
import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
import org.testinfected.petstore.dispatch.Dispatch;
import org.testinfected.petstore.dispatch.EndPoint;
import org.testinfected.petstore.util.ContextBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.testinfected.petstore.util.ContextBuilder.context;

public class ShowProducts implements EndPoint {

    private final ProductCatalog productCatalog;
    private final AttachmentStorage attachments;

    public ShowProducts(ProductCatalog productCatalog, AttachmentStorage attachments) {
        this.productCatalog = productCatalog;
        this.attachments = attachments;
    }

    public void process(Dispatch.Request request, Dispatch.Response response) throws Exception {
        String keyword = request.getParameter("keyword");
        List<Product> matchingProducts = productCatalog.findByKeyword(keyword);

        ContextBuilder context = context().with("keyword", keyword);
        if (matchingProducts.isEmpty()) {
            response.render("no-product", context.asMap());
        } else {
            response.render("products", context.
                    with("products", withPhotos(matchingProducts)).
                    and("matchCount", matchingProducts.size()).
                    asMap());
        }
    }

    private List<ProductAndPhoto> withPhotos(List<Product> products) {
        List<ProductAndPhoto> productAndPhotos = new ArrayList<ProductAndPhoto>();
        for (Product product : products) {
            productAndPhotos.add(new ProductAndPhoto(product, product.getPhotoLocation(attachments)));
        }
        return productAndPhotos;
    }

    public static class ProductAndPhoto {
        private final Product product;
        private final String photo;

        public ProductAndPhoto(Product product, String photo) {
            this.product = product;
            this.photo = photo;
        }

        public String getNumber() {
            return product.getNumber();
        }

        public String getDescription() {
            return product.getDescription();
        }

        public String getName() {
            return product.getName();
        }

        public String getPhoto() {
            return photo;
        }
    }
}
