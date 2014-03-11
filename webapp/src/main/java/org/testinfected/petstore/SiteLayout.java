package org.testinfected.petstore;

import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.Session;
import org.testinfected.molecule.decoration.Layout;
import org.testinfected.molecule.middlewares.AbstractMiddleware;
import org.testinfected.molecule.middlewares.FilterMap;
import org.testinfected.molecule.middlewares.SiteMesh;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.util.SessionScope;
import org.testinfected.petstore.views.PlainPage;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class SiteLayout extends AbstractMiddleware {

    private RenderingEngine renderer;

    public SiteLayout(RenderingEngine renderer) {
        this.renderer = renderer;
    }

    public void handle(Request request, Response response) throws Exception {
        FilterMap filtering = new FilterMap();
        filtering.map("/", SiteMesh.html(new LayoutTemplate("main", renderer, cartFor(request))));
        filtering.connectTo(successor);
        filtering.handle(request, response);
    }

    private Cart cartFor(Request client) {
        Session session = client.session(false);
        return session != null ? SessionScope.cart(client) : new Cart();
    }

    public static class LayoutTemplate implements Layout {
        private final String template;
        private final RenderingEngine renderer;
        private final Cart cart;

        public LayoutTemplate(String template, RenderingEngine renderer, Cart cart) {
            this.template = template;
            this.renderer = renderer;
            this.cart = cart;
        }

        public void render(Writer out, Map<String, String> fragments) throws IOException {
            renderer.render(out, template, new PlainPage().composedOf(fragments).withCart(cart));
        }
    }
}
