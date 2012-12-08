package org.testinfected.petstore.util;

import org.testinfected.petstore.order.Cart;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Session;

public class SessionScope {

    private final Session session;

    public SessionScope(Session session) {
        this.session = session;
    }

    public static SessionScope sessionScopeOf(Request request) throws Exception {
        return new SessionScope(request.session());
    }

    @SuppressWarnings("unchecked")
    public Cart cart() {
        if (!session.contains(Cart.class)) {
            session.put(Cart.class, new Cart());
        }
        return (Cart) session.get(Cart.class);
    }
}
