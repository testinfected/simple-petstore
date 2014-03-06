package org.testinfected.petstore.util;

import org.testinfected.molecule.Request;
import org.testinfected.molecule.Session;
import org.testinfected.petstore.order.Cart;

public class SessionScope {

    public static Cart cart(Request client) {
        return sessionOf(client).cart();
    }

    private static SessionScope sessionOf(Request client) {
        return new SessionScope(client.session());
    }

    private final Session session;

    public SessionScope(Session session) {
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    public Cart cart() {
        if (!session.contains(Cart.class)) {
            session.put(Cart.class, new Cart());
        }
        return session.get(Cart.class);
    }
}
