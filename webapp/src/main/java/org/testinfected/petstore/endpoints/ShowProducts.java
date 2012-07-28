package org.testinfected.petstore.endpoints;

import com.github.mustachejava.TemplateFunction;
import com.pyxis.petstore.domain.product.AttachmentStorage;
import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
import org.testinfected.petstore.dispatch.Dispatch;
import org.testinfected.petstore.dispatch.EndPoint;
import org.testinfected.petstore.util.ContextBuilder;

import javax.annotation.Nullable;
import java.util.List;

import static org.testinfected.petstore.util.ContextBuilder.context;

public class ShowProducts implements EndPoint {

    private final ProductCatalog productCatalog;
    private final AttachmentStorage storage;

    public ShowProducts(ProductCatalog productCatalog, AttachmentStorage storage) {
        this.productCatalog = productCatalog;
        this.storage = storage;
    }

    public void process(Dispatch.Request request, Dispatch.Response response) throws Exception {
        String keyword = request.getParameter("keyword");
        List<Product> matchingProducts = productCatalog.findByKeyword(keyword);

        ContextBuilder context = context().with("keyword", keyword);
        if (matchingProducts.isEmpty()) {
            response.render("no-results", context.asMap());
        } else {
            response.render("products", context.
                    with("products", matchingProducts).
                    and("matchCount", matchingProducts.size()).
                    and("photo", LocateAttachment.in(storage)).asMap());
        }
    }

    private static class LocateAttachment implements TemplateFunction {
        public static LocateAttachment in(AttachmentStorage storage) {
            return new LocateAttachment(storage);
        }

        private final AttachmentStorage attachments;

        public LocateAttachment(AttachmentStorage attachments) {
            this.attachments = attachments;
        }

        public String apply(@Nullable String fileName) {
            return attachments.getLocation(fileName);
        }
    }
}
