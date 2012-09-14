package org.testinfected.petstore.endpoints;

import com.github.mustachejava.TemplateFunction;
import com.pyxis.petstore.domain.product.AttachmentStorage;
import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
import org.testinfected.petstore.dispatch.Dispatch;
import org.testinfected.petstore.dispatch.EndPoint;
import org.testinfected.petstore.util.Context;

import java.util.List;

import static org.testinfected.petstore.util.Context.context;

public class ShowProducts implements EndPoint {

    private final ProductCatalog productCatalog;
    private final AttachmentStorage attachmentStorage;

    public ShowProducts(ProductCatalog productCatalog, AttachmentStorage attachmentStorage) {
        this.productCatalog = productCatalog;
        this.attachmentStorage = attachmentStorage;
    }

    public void process(Dispatch.Request request, Dispatch.Response response) throws Exception {
        String keyword = request.getParameter("keyword");
        List<Product> products = productCatalog.findByKeyword(keyword);

        Context context = context().
                with("match-found", !products.isEmpty()).
                and("keyword", keyword).
                and("products", products).
                and("matchCount", products.size()).
                and("photo", LocateAttachment.in(attachmentStorage));
        response.render("products", context.asMap());
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
