package org.testinfected.petstore;

import org.testinfected.petstore.order.Cart;
import org.testinfected.support.decoration.Layout;
import org.testinfected.support.RenderingEngine;
import org.testinfected.support.Request;
import org.testinfected.support.Response;
import org.testinfected.support.middlewares.AbstractMiddleware;
import org.testinfected.support.middlewares.FilterMap;
import org.testinfected.support.middlewares.SiteMesh;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import static org.testinfected.petstore.util.SessionScope.sessionScopeOf;

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

    public static class MainLayout implements Layout {
        private final String template;
        private final RenderingEngine renderer;
        private final Cart cart;

        public MainLayout(String template, RenderingEngine renderer, Cart cart) {
            this.template = template;
            this.renderer = renderer;
            this.cart = cart;
        }

        public void render(Writer out, Map<String, Object> fragments) throws IOException {
            fragments.put("cart", cart);
            renderer.render(out, template, fragments);
        }
    }
}
