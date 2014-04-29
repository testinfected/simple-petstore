package org.testinfected.petstore;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.Session;
import com.vtence.molecule.decoration.Layout;
import com.vtence.molecule.middlewares.AbstractMiddleware;
import com.vtence.molecule.middlewares.FilterMap;
import com.vtence.molecule.middlewares.SiteMesh;
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
