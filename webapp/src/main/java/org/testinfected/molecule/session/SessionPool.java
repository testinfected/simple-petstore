package org.testinfected.molecule.session;

import org.testinfected.molecule.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionPool implements SessionStore {

    private final Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();
    private final SessionListener listener;

    public SessionPool() {
        this(SessionListener.NONE);
    }

    public SessionPool(SessionListener listener) {
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
        Session session = new SessionHash(key);
        sessions.put(key, session);
        notifyCreated(session);
        return session;
    }

    private void notifyCreated(Session session) {
        listener.sessionCreated(session);
    }
}
