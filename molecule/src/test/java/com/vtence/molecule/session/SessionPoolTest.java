package com.vtence.molecule.session;

import com.vtence.molecule.Session;
import com.vtence.molecule.support.Delorean;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.lang.String.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SessionPoolTest {

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    SessionIdentifierPolicy counter = new Sequence();
    Delorean delorean = new Delorean();
    int maxAge = (int) TimeUnit.MINUTES.toSeconds(30);

    SessionPool pool = new SessionPool(counter, delorean);
    SessionPoolListener listener = context.mock(SessionPoolListener.class);

    @Test public void
    isInitiallyEmpty() {
        assertThat("initial pool size", pool.size(), equalTo(0));
        assertThat("session not in pool", pool.load("not-in-pool"), nullValue());
    }

    @Test public void
    generatesIdsForNewSessions() {
        Session data = new Session();
        String id = pool.save(data);
        Session session = pool.load(id);
        assertThat("created session", session, notNullValue());
        assertThat("generated id", id, equalTo("1"));
    }

    @Test public void
    preservesExistingSessionsIds() {
        Session data = new Session();
        String id = pool.save(data);
        Session session = pool.load(id);
        assertThat("original id", pool.save(session), equalTo(id));
    }

    @Test public void
    replacesIdsOfSessionsNotInPool() {
        Session data = new Session();
        String id = pool.save(data);
        assertThat("original id", id, equalTo("1"));
        Session session = pool.load(id);
        pool.clear();
        assertThat("replacement id", pool.save(session), equalTo("2"));
    }

    @Test public void
    savesSessionContentDefensively() {
        Session data = new Session();
        data.put("a", "Alice");
        data.put("b", "Bob");
        data.put("c", "Chris");
        data.maxAge(maxAge);

        Session saved = save(data);
        data.clear();

        assertThat("saved session", saved, not(Matchers.sameInstance(data)));
        assertThat("saved session expiration time", saved.maxAge(), equalTo(maxAge));
        assertThat("saved session keys", saved.keys(), Matchers.<Object>contains("a", "b", "c"));
        assertThat("saved session values", saved.values(), Matchers.<Object>contains("Alice", "Bob", "Chris"));
    }

    @Test public void
    loadsSessionContentDefensively() {
        Session data = new Session();
        data.put("a", "Alice");
        data.put("b", "Bob");
        data.put("c", "Chris");

        Session loaded = save(data);
        loaded.clear();

        Session stored = pool.load(loaded.id());
        assertThat("stored session values", stored.size(), equalTo(data.size()));
        assertThat("stored session values", stored.values(), Matchers.<Object>contains("Alice", "Bob", "Chris"));
    }

    @Test public void
    storesMultipleSessions() {
        int count = 5;
        for (int i = 1; i <= count; i++) {
            String id = pool.save(new Session());
            assertThat("session #" + id, pool.load(id), sessionWithId(valueOf(i)));
        }
        assertThat("pool size", pool.size(), equalTo(count));
    }

    @Test(expected = IllegalStateException.class) public void
    forbidsSavingInvalidSessions() {
        Session data = new Session();
        data.invalidate();
        pool.save(data);
    }

    @Test public void
    marksSessionUpdateTime() throws InterruptedException {
        Session data = new Session();
        Date updateTime = delorean.freeze();
        Session session = save(data);
        assertThat("update time", session.updatedAt(), equalTo(updateTime));
    }

    @Test public void
    marksSessionCreationTime() throws InterruptedException {
        Session data = new Session();
        Date creationTime = delorean.freeze();
        Session session = save(data);
        assertThat("creation time", session.createdAt(), equalTo(creationTime));

        delorean.unfreeze();
        delorean.travelInTime(1000);

        Session laterOn = save(session);
        assertThat("creation time later on", laterOn.createdAt(), equalTo(creationTime));
    }

    @Test public void
    discardsExpiredSessions() {
        Session data = new Session();
        data.maxAge(maxAge);
        String sid = pool.save(data);
        delorean.travelInTime(timeJump(maxAge));
        assertThat("expired session", pool.load(sid), nullValue());
    }

    @Test public void
    destroysExpiredSessionsDuringHouseKeeping() {
        Collection<String> persistentSessions = addSessionsToPool(10);
        Collection<String> expiringSessions = expire(addSessionsToPool(10));

        delorean.travelInTime(timeJump(maxAge));
        pool.houseKeeping();

        assertThat("cleaned pool size", pool.size(), equalTo(persistentSessions.size()));
        assertStillInPool(persistentSessions);
        assertNoLongerInPool(expiringSessions);
    }

    @Test public void
    notifiesWhenSessionsAreLoaded() {
        final String sid = pool.save(new Session());
        pool.setSessionListener(listener);
        context.checking(new Expectations() {{
            oneOf(listener).sessionLoaded(with(sid));
        }});
        pool.load(sid);
    }

    @Test public void
    notifiesWhenSessionsAreSaved() {
        final String sid = pool.save(new Session());
        Session session = pool.load(sid);

        pool.setSessionListener(listener);
        context.checking(new Expectations() {{
            oneOf(listener).sessionSaved(with(sid));
        }});
        pool.save(session);
    }

    @Test public void
    notifiesWhenSessionsAreCreated() {
        pool.setSessionListener(listener);
        context.checking(new Expectations() {{
            oneOf(listener).sessionCreated(with("1"));
        }});
        pool.save(new Session());
    }

    @Test public void
    notifiesWhenSessionsAreDropped() {
        final String sid = pool.save(new Session());
        pool.setSessionListener(listener);

        pool.destroy("not-in-pool");
        context.checking(new Expectations() {{
            oneOf(listener).sessionDropped(with(sid));
        }});
        pool.destroy(sid);
    }

    private long timeJump(int seconds) {
        return TimeUnit.SECONDS.toMillis(seconds);
    }

    private Collection<String> expire(Collection<String> sessions) {
        for (String sid : sessions) {
            Session session = pool.load(sid);
            session.maxAge(maxAge);
            pool.save(session);
        }
        return sessions;
    }

    private Collection<String> addSessionsToPool(int count) {
        Collection<String> sessions = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            Session data = new Session();
            sessions.add(pool.save(data));
        }
        return sessions;
    }

    private Session save(Session data) {
        String id = pool.save(data);
        return pool.load(id);
    }

    private Matcher<Session> sessionWithId(final String id) {
        return new FeatureMatcher<Session, String>(equalTo(id), "session with id", "id") {
            protected String featureValueOf(Session actual) {
                return actual.id();
            }
        };
    }

    private void assertStillInPool(Iterable<String> sessions) {
        for (String sid : sessions) {
            assertThat("session #" + sid, pool.load(sid), notNullValue());
        }
    }

    private void assertNoLongerInPool(Iterable<String> sessions) {
        for (String sid : sessions) {
            assertThat("session #" + sid, pool.load(sid), nullValue());
        }
    }

    private class Sequence implements SessionIdentifierPolicy {
        private int nextId;

        private Sequence() {
            this(1);
        }

        public Sequence(int seed) {
            this.nextId = seed;
        }

        public String generateId() {
            return valueOf(nextId++);
        }
    }
}