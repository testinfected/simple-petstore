package org.testinfected.molecule.session;

import org.testinfected.molecule.Session;

public interface SessionListener {

    void sessionCreated(Session session);

    void sessionAccessed(Session session);

    void sessionDropped(Session session);

    public static final SessionListener NONE = new None();

    public static final class None implements SessionListener {

        public void sessionCreated(Session session) {
        }

        public void sessionAccessed(Session session) {
        }

        public void sessionDropped(Session session) {
        }
    }
}
