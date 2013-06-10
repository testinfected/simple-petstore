package org.testinfected.molecule.session;

import org.testinfected.molecule.Session;
import org.testinfected.molecule.util.Clock;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class SessionHash implements Session {

    private final String id;
    private final Date createdAt;
    private final Map<Object, Object> attributes = new ConcurrentHashMap<Object, Object>();

    private long timeoutInMillis;
    private Date lastAccessedAt;
    private boolean invalid;

    public SessionHash(String id, Date createdAt) {
        this(id, createdAt, createdAt);
    }

    public SessionHash(String id, Date createdAt, Date lastAccessedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.lastAccessedAt = lastAccessedAt;
    }

    public String id() {
        return id;
    }

    public boolean contains(Object key) {
        return attributes.containsKey(key);
    }

    public void put(Object key, Object value) {
        attributes.put(key, value);
    }

    public Object get(Object key) {
        return attributes.get(key);
    }

    public Set<Object> keys() {
        return Collections.unmodifiableSet(attributes.keySet());
    }

    public Collection<Object> values() {
        return Collections.unmodifiableCollection(attributes.values());
    }

    public void timeout(int inSeconds) {
        this.timeoutInMillis = TimeUnit.SECONDS.toMillis(inSeconds);
    }

    public boolean expired(Clock clock) {
        if (timeoutInMillis == 0) return false;
        return !clock.now().before(expirationTime());
    }

    public void touch(Clock clock) {
        lastAccessedAt = clock.now();
    }

    public void invalidate() {
        attributes.clear();
        this.invalid = true;
    }

    public boolean invalid() {
        return invalid;
    }

    public Date createdAt() {
        return createdAt;
    }

    public Date lastAccessedAt() {
        return lastAccessedAt;
    }

    private Date expirationTime() {
        return new Date(lastAccessedAt.getTime() + timeoutInMillis);
    }

    public String toString() {
        return id + ": " + attributes.toString();
    }
}