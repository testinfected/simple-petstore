package org.testinfected.petstore.util;

import org.testinfected.molecule.Session;
import org.testinfected.petstore.order.Cart;

public class SessionScope {

    private final Session session;

    public SessionScope(Session session) {
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    public Cart cart() {
        if (!session.contains(Cart.class)) {
            session.put(Cart.class, new Cart());
        }
        return (Cart) session.get(Cart.class);
    }
}
