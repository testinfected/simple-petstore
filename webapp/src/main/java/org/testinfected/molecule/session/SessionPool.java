package org.testinfected.molecule.session;

import org.testinfected.molecule.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionPool implements SessionStore {

    private final Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();

    public Session load(String key, boolean create) {
        if (hasSession(key)) return locateSession(key);
        if (!create) return null;
        return createSession(key);
    }

    private boolean hasSession(String key) {
        return sessions.containsKey(key);
    }

    private Session locateSession(String key) {
        return sessions.get(key);
    }

    private Session createSession(String key) {
        Session session = new SessionHash(key);
        sessions.put(key, session);
        return session;
    }
}
