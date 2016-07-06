package org.testinfected.petstore.lib;

import com.vtence.molecule.Request;
import com.vtence.molecule.session.Session;
import org.testinfected.petstore.order.Cart;

public class SessionScope {

    public static Cart cart(Request client) {
        return get(client).cart();
    }

    public static SessionScope get(Request client) {
        return new SessionScope(Session.get(client));
    }

    private final Session session;

    public SessionScope(Session session) {
        this.session = session;
    }

    public Cart cart() {
        if (!session.contains(Cart.class)) {
            session.put(Cart.class, new Cart());
        }
        return session.get(Cart.class);
    }
}
