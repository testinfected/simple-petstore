package com.vtence.molecule.session;

import com.vtence.molecule.Session;

public interface SessionStore {

    Session create(String key);

    Session load(String key);
}
