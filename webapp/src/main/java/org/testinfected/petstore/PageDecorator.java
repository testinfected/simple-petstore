package org.testinfected.petstore;

import com.vtence.molecule.Body;
import com.vtence.molecule.Request;
import com.vtence.molecule.decoration.Decorator;
import com.vtence.molecule.session.Session;
import com.vtence.molecule.templating.Template;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.views.PlainPage;

import java.io.IOException;
import java.util.Map;

public class PageDecorator implements Decorator {

    private final Template<PlainPage> layout;

    public PageDecorator(Template<PlainPage> layout) {
        this.layout = layout;
    }

    public Body merge(Request request, Map<String, String> content) throws IOException {
        Session session = Session.get(request);
        Cart cart = session.contains(Cart.class) ? session.get(Cart.class) : new Cart();
        return layout.render(new PlainPage().composedOf(content).withCart(cart));
    }
}