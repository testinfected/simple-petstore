package org.testinfected.petstore;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.decoration.Template;
import org.testinfected.petstore.middlewares.AbstractMiddleware;
import org.testinfected.petstore.middlewares.FilterMap;
import org.testinfected.petstore.decoration.SiteMesh;
import org.testinfected.petstore.order.Cart;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import static org.testinfected.petstore.SessionScope.sessionScopeOf;

public class SiteLayout extends AbstractMiddleware {

    private RenderingEngine renderer;

    public SiteLayout(RenderingEngine renderer) {
        this.renderer = renderer;
    }

    public void handle(Request request, Response response) throws Exception {
        Cart cart = sessionScopeOf(request).cart();
        FilterMap filtering = new FilterMap();
        filtering.map("/", SiteMesh.html(new MainLayout("main", renderer, cart)));
        filtering.connectTo(successor);
        filtering.handle(request, response);
    }

    public static class MainLayout extends Template<Map<String, Object>> {
        private final Cart cart;

        public MainLayout(String template, RenderingEngine renderer, Cart cart) {
            super(template, renderer);
            this.cart = cart;
        }

        public void render(Writer out, Map<String, Object> context) throws IOException {
            context.put("cart", cart);
            super.render(out, context);
        }
    }
}
