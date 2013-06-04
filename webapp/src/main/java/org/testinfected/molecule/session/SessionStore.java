package org.testinfected.molecule.session;

import org.testinfected.molecule.Session;

public interface SessionStore {

    Session load(String key, boolean create);
}
