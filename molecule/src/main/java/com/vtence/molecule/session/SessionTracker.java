package com.vtence.molecule.session;

import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.Session;

public interface SessionTracker {

    Session acquireSession(Request request, Response response);

    Session openSession(Request request, Response response);
}
