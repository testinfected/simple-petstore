package com.vtence.molecule.session;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.Session;

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
