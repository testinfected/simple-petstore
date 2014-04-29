package org.testinfected.petstore;

import com.vtence.molecule.Body;
import com.vtence.molecule.Request;
import com.vtence.molecule.Session;
import com.vtence.molecule.decoration.Decorator;
import com.vtence.molecule.templating.Template;
import org.testinfected.petstore.order.Cart;
import org.testinfected.petstore.views.PlainPage;

import java.io.IOException;
import java.util.Map;

public class SiteLayout implements Decorator {

    private final Template layout;

    public SiteLayout(Template layout) {
        this.layout = layout;
    }

    public Body merge(Request request, Map<String, String> content) throws IOException {
        Session session = Session.get(request);
        Cart cart = session.contains(Cart.class) ? session.<Cart>get(Cart.class) : new Cart();
        return layout.render(new PlainPage().composedOf(content).withCart(cart));
    }
}