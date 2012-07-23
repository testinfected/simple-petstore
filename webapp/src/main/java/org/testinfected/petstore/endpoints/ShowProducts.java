package org.testinfected.petstore.endpoints;

import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.dispatch.Dispatch;
import org.testinfected.petstore.dispatch.EndPoint;
import org.testinfected.petstore.pipeline.Dispatcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testinfected.petstore.util.ContextBuilder.context;

public class ShowProducts implements EndPoint {

    private final ProductCatalog productCatalog;

    public ShowProducts(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    public void process(Request request, Response response, Dispatcher dispatcher) throws IOException {
        Map<String, String> context = new HashMap<String, String>();
        dispatcher.renderTemplate("pages/products", context, response);
    }

    public void process(Dispatch.Request request, Dispatch.Response response) throws Exception {
        String keyword = request.getParameter("keyword");
        List<Product> matchingProducts = productCatalog.findByKeyword(keyword);

        response.render("pages/products", context().
                with("products", matchingProducts).
                and("keyword", keyword).asMap());
    }
}
