package com.vtence.molecule.session;

import com.vtence.molecule.Session;
import com.vtence.molecule.util.Clock;
import com.vtence.molecule.util.SystemClock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class SessionPool implements SessionStore, SessionHouse {

    private static final long HALF_AN_HOUR = TimeUnit.MINUTES.toSeconds(30);

    private final Clock clock;
    private final SessionListener listener;
    private final Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();

    private long timeout;

    public SessionPool() {
        this(HALF_AN_HOUR);
    }

    public SessionPool(long timeoutInSeconds) {
        this(new SystemClock(), timeoutInSeconds);
    }

    public SessionPool(Clock clock, long timeoutInSeconds) {
        this(clock, timeoutInSeconds, SessionListener.NONE);
    }

    public SessionPool(Clock clock, long timeoutInSeconds, SessionListener listener) {
        this.clock = clock;
        this.timeout = timeoutInSeconds;
        this.listener = listener;
    }

    public Session load(String key) {
        return locateSession(key);
    }

    public Session create(String key) {
        return createSession(key);
    }

    private Session locateSession(String key) {
        Session session = validate(sessions.get(key));
        if (session == null) return null;
        session.touch(clock);
        listener.sessionAccessed(session);
        return session;
    }

    private Session createSession(String key) {
        Session session = new SessionHash(key, clock.now());
        session.timeout(timeout);
        sessions.put(key, session);
        listener.sessionCreated(session);
        return session;
    }

    public void houseKeeping() {
        for (Session session : sessions.values()) {
            validate(session);
        }
    }

    private Session validate(Session session) {
        if (session == null) return null;
        if (session.expired(clock)) session.invalidate();
        if (session.invalid()) remove(session);
        return session.invalid() ? null : session;
    }

    private void remove(Session session) {
        sessions.remove(session.id());
        listener.sessionDropped(session);
    }
}
