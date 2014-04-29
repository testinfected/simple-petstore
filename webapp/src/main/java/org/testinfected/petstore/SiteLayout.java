package org.testinfected.petstore;

import com.vtence.molecule.Body;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.Session;
import com.vtence.molecule.decoration.Decorator;
import com.vtence.molecule.lib.AbstractMiddleware;
import com.vtence.molecule.middlewares.FilterMap;
import com.vtence.molecule.middlewares.Layout;
import com.vtence.molecule.templating.RenderingEngine;
import com.vtence.molecule.templating.TemplateBody;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.util.SessionScope;
import org.testinfected.petstore.views.PlainPage;

import java.io.IOException;
import java.util.Map;

public class SiteLayout extends AbstractMiddleware {

    private RenderingEngine renderer;

    public SiteLayout(RenderingEngine renderer) {
        this.renderer = renderer;
    }

    public void handle(Request request, Response response) throws Exception {
        FilterMap filtering = new FilterMap();
        filtering.map("/", Layout.html(new LayoutTemplate("main", renderer, cartFor(request))));
        filtering.connectTo(successor);
        filtering.handle(request, response);
    }

    private Cart cartFor(Request client) {
        Session session = Session.get(client);
        return session.contains(Cart.class) ? SessionScope.cart(client) : new Cart();
    }

    public static class LayoutTemplate implements Decorator {
        private final String template;
        private final RenderingEngine renderer;
        private final Cart cart;

        public LayoutTemplate(String template, RenderingEngine renderer, Cart cart) {
            this.template = template;
            this.renderer = renderer;
            this.cart = cart;
        }

        public Body merge(Request request, Map<String, String> content) throws IOException {
            return new TemplateBody(renderer, template, new PlainPage().composedOf(content).withCart(cart));
        }
    }
}
