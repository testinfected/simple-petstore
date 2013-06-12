package org.testinfected.molecule.session;

import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.Session;

public class CookieTracker implements SessionTracker {

    private static final String STANDARD_SERVLET_SESSION_COOKIE = "JSESSIONID";

    private final SessionStore store;
    private final SessionIdentifierPolicy policy;
    private final String cookieName;

    public CookieTracker(SessionStore store, SessionIdentifierPolicy policy) {
        this(store, policy, STANDARD_SERVLET_SESSION_COOKIE);
    }

    public CookieTracker(SessionStore store, SessionIdentifierPolicy policy, String cookieName) {
        this.store = store;
        this.policy = policy;
        this.cookieName = cookieName;
    }

    public Session acquireSession(Request request, Response response) {
        String sessionId = request.cookie(cookieName);
        return sessionId != null ? store.load(sessionId) : null;
    }

    public Session openSession(Request request, Response response) {
        Session session = store.create(policy.generateId());
        response.cookie(cookieName, session.id());
        return session;
    }
}
