package org.testinfected.molecule.session;

import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.Session;

public interface SessionTracker {

    Session acquireSession(Request request, Response response);

    Session openSession(Request request, Response response);
}
