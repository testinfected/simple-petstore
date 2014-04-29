package com.vtence.molecule.session;

import com.vtence.molecule.Session;

public interface SessionStore {

    Session load(String id);

    String save(Session session);

    void destroy(String sid);
}
