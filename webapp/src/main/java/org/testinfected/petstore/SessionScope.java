package org.testinfected.petstore;

import org.testinfected.petstore.order.Cart;
import org.simpleframework.http.Request;
import org.simpleframework.http.session.Session;

public class SessionScope {

    private final Session session;

    public SessionScope(Session session) {
        this.session = session;
    }

    public static SessionScope sessionScopeOf(Request request) throws Exception {
        return new SessionScope(request.getSession());
    }

    @SuppressWarnings("unchecked")
    public Cart cart() {
        if (!session.containsKey(Cart.class)) {
            session.put(Cart.class, new Cart());
        }
        return (Cart) session.get(Cart.class);
    }
}
