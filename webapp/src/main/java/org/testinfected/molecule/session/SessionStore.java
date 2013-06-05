package org.testinfected.molecule.session;

import org.testinfected.molecule.Session;

public interface SessionStore {

    Session create(String key);

    Session load(String key);
}
