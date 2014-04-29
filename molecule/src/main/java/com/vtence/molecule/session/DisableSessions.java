package com.vtence.molecule.session;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.Session;

public class DisableSessions implements SessionTracker {

    public Session acquireSession(Request request, Response response) {
        throw new UnsupportedOperationException("Sessions are not enabled");
    }

    public Session openSession(Request request, Response response) {
        throw new UnsupportedOperationException("Sessions are not enabled");
    }
}
