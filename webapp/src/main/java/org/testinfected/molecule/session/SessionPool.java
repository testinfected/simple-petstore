package org.testinfected.molecule.session;

import org.testinfected.molecule.Session;
import org.testinfected.molecule.util.Clock;
import org.testinfected.molecule.util.SystemClock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionPool implements SessionStore {

    private final Clock clock;
    private final SessionListener listener;
    private final Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();

    public SessionPool() {
        this(new SystemClock());
    }

    public SessionPool(Clock clock) {
        this(clock, SessionListener.NONE);
    }

    public SessionPool(SessionListener listener) {
        this(new SystemClock(), listener);
    }

    public SessionPool(Clock clock, SessionListener listener) {
        this.clock = clock;
        this.listener = listener;
    }

    public Session load(String key) {
        return locateSession(key);
    }

    public Session create(String key) {
        return createSession(key);
    }

    private Session locateSession(String key) {
        Session session = sessions.get(key);
        notifyAccessed(session);
        return session;
    }

    private void notifyAccessed(Session session) {
        if (session != null) listener.sessionAccessed(session);
    }

    private Session createSession(String key) {
        Session session = new SessionHash(key, clock.now());
        sessions.put(key, session);
        notifyCreated(session);
        return session;
    }

    private void notifyCreated(Session session) {
        listener.sessionCreated(session);
    }
}
