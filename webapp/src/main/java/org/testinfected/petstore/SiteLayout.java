package org.testinfected.petstore;

import org.testinfected.petstore.order.Cart;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.decoration.HtmlDocumentProcessor;
import org.testinfected.petstore.decoration.HtmlPageSelector;
import org.testinfected.petstore.decoration.PageCompositor;
import org.testinfected.petstore.decoration.Template;
import org.testinfected.petstore.decoration.View;
import org.testinfected.petstore.middlewares.AbstractMiddleware;
import org.testinfected.petstore.middlewares.SiteMesh;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import static org.testinfected.petstore.SessionScope.inSessionOf;

public class SiteLayout extends AbstractMiddleware {

    private RenderingEngine renderer;

    public SiteLayout(RenderingEngine renderer) {
        this.renderer = renderer;
    }

    public void handle(Request request, Response response) throws Exception {
        SiteMesh siteMesh = new SiteMesh(new HtmlPageSelector());
        MainLayout main = new MainLayout(inSessionOf(request).cart(), new Template<Map<String, Object>>("main", renderer));
        siteMesh.map("/", new PageCompositor(new HtmlDocumentProcessor(), main));
        siteMesh.connectTo(successor);
        siteMesh.handle(request, response);
    }

    public static class MainLayout implements View<Map<String, Object>> {

        private final Cart cart;
        private final View<Map<String, Object>> view;

        public MainLayout(Cart cart, View<Map<String, Object>> layout) {
            this.cart = cart;
            this.view = layout;
        }

        public void render(Writer out, Map<String, Object> context) throws IOException {
            context.put("cart", cart);
            view.render(out, context);
        }
    }
}
