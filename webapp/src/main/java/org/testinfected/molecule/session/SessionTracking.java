package org.testinfected.molecule.session;

import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.Session;

public class SessionTracking {

    private final SessionTracker tracker;
    private final Response response;

    private Session session;

    public SessionTracking(SessionTracker tracker, Response response) {
        this.tracker = tracker;
        this.response = response;
    }

    public Session openSession(Request request, boolean create) {
        if (session != null) return session;
        session = tracker.acquireSession(request, response);
        if (session == null && create) session = tracker.openSession(request, response);
        return session;
    }
}
