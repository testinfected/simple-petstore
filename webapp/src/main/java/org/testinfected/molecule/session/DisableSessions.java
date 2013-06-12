package org.testinfected.molecule.session;

import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.Session;

public class DisableSessions implements SessionTracker {

    public Session acquireSession(Request request, Response response) {
        throw new UnsupportedOperationException("Sessions are not enabled");
    }

    public Session openSession(Request request, Response response) {
        throw new UnsupportedOperationException("Sessions are not enabled");
    }
}
