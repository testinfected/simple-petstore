package com.vtence.molecule.session;

import com.vtence.molecule.support.BrokenClock;
import com.vtence.molecule.support.DateBuilder;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import com.vtence.molecule.Session;
import com.vtence.molecule.util.Clock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.vtence.molecule.support.DateBuilder.namedDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

public class SessionPoolTest {

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    long timeout = TimeUnit.MINUTES.toSeconds(30);
    DateBuilder now = namedDate("now").atTime(11, 00, 00);
    DateBuilder withinExpirationDelay = namedDate("5 minutes ago").atTime(10, 55, 00);
    DateBuilder pastExpirationDelay = now.but().atTime(9, 00, 00).named("2 hours ago");

    SessionListener listener = context.mock(SessionListener.class);
    SessionPool pool = new SessionPool(clockSetAt(now), timeout, listener);

    @Test public void
    isInitiallyEmpty() {
        context.checking(new Expectations() {{
            never(listener);
        }});
        Session session = pool.load("session");
        assertThat("session", session, nullValue());
    }

    @Test public void
    createsEmptySessionHashes() {
        ignoreNotifications();
        Session session = pool.create("session");
        assertThat("session", session, instanceOf(SessionHash.class));
        assertThat("session creation time", session.createdAt(), equalTo(now.build()));
        assertThat("session timeout", session.timeout(), equalTo(timeout));
        assertThat("session keys", session.keys(), emptyIterable());
    }

    @Test public void
    notifiesWhenSessionsAreCreated() {
        context.checking(new Expectations() {{
            oneOf(listener).sessionCreated(with(sessionWithId("key")));
        }});
        pool.create("key");
    }

    @Test public void
    notifiesWhenSessionsAreAccessed() {
        addToPool("key");
        context.checking(new Expectations() {{
            oneOf(listener).sessionAccessed(with(sessionWithId("key")));
        }});
        pool.load("key");
    }

    @Test public void
    storesAndRetrievesMultipleSessions() {
        ignoreNotifications();
        Session session1 = pool.create("session1");
        Session session2 = pool.create("session2");
        Session session3 = pool.create("session3");

        assertThat("session 1", pool.load("session1"), equalTo(session1));
        assertThat("session 2", pool.load("session2"), equalTo(session2));
        assertThat("session 3", pool.load("session3"), equalTo(session3));
    }

    @Test public void
    touchesSessionsOnAccess() {
        ignoreNotifications();
        for (Session session : addToPool("session")) {
            session.touch(clockSetAt(withinExpirationDelay));
        }
        assertThat("session last access", pool.load("session").lastAccessedAt(), equalTo(now.build()));
    }

    @Test public void
    removesExpiredSessionOnAccess() {
        ignoreNotifications();
        Collection<Session> expiredSessions = expire(addToPool("expired session"));
        assertNoLongerInPool(expiredSessions);
    }

    @Test public void
    removesInvalidSessionOnAccess() {
        ignoreNotifications();
        Collection<Session> invalidSessions = invalidate(addToPool("invalid session"));
        assertNoLongerInPool(invalidSessions);
    }

    @Test public void
    removesInvalidSessionsDuringHouseKeeping() {
        ignoreNotifications();
        Collection<Session> validSessions = addToPool(sessions("valid", 10));
        Collection<Session> invalidSessions = invalidate(addToPool(sessions("invalid", 10)));

        pool.houseKeeping();

        assertStillInPool(validSessions);
        assertNoLongerInPool(invalidSessions);
    }

    @Test public void
    invalidatesAndRemovesExpiredSessionsDuringHouseKeeping() {
        ignoreNotifications();
        Collection<Session> validSessions = addToPool(sessions("valid", 10));
        Collection<Session> expiredSessions = expire(addToPool(sessions("expired", 10)));

        pool.houseKeeping();

        assertStillInPool(validSessions);
        assertNoLongerInPool(expiredSessions);
        assertInvalid(expiredSessions);
    }

    @Test public void
    notifiesWhenSessionsAreDropped() {
        ignoreCreation();

        expire(addToPool("invalid"));
        context.checking(new Expectations() {{
            oneOf(listener).sessionDropped(with(sessionWithId("invalid")));
        }});
        pool.load("invalid");
    }

    private Clock clockSetAt(DateBuilder date) {
        return BrokenClock.stoppedAt(date.build());
    }

    private Collection<Session> invalidate(Collection<Session> sessions) {
        for (Session session : sessions) {
            session.invalidate();
        }
        return sessions;
    }

    private Collection<Session> expire(Collection<Session> sessions) {
        for (Session session : sessions) {
            session.touch(clockSetAt(pastExpirationDelay));
        }
        return sessions;
    }

    private String[] sessions(String name, int count) {
        List<String> keys = new ArrayList<String>();
        for (int i = 1; i <= count; i++) {
            keys.add(name + "-" + i);
        }
        return keys.toArray(new String[count]);
    }

    private Collection<Session> addToPool(String... keys) {
        ignoreCreation();
        Collection<Session> sessions = new ArrayList<Session>();
        for (String key : keys) {
            sessions.add(pool.create(key));
        }
        return sessions;
    }

    private void ignoreCreation() {
        context.checking(new Expectations() {{
            ignoring(listener).sessionCreated(with(any(Session.class)));
        }});
    }

    private void ignoreNotifications() {
        context.checking(new Expectations() {{
            ignoring(listener);
        }});
    }

    private Matcher<Session> sessionWithId(final String id) {
        return new FeatureMatcher<Session, String>(equalTo(id), "session with id", "id") {
            protected String featureValueOf(Session actual) {
                return actual.id();
            }
        };
    }

    private void assertInvalid(Iterable<Session> sessions) {
        for (Session session : sessions) {
            assertThat(session.id() + " valid?", !session.invalid(), equalTo(false));
        }
    }

    private void assertNoLongerInPool(Iterable<Session> sessions) {
        for (Session session : sessions) {
            assertThat(session.id(), pool.load(session.id()), nullValue());
        }
    }

    private void assertStillInPool(Iterable<Session> sessions) {
        for (Session session : sessions) {
            assertThat(session.id(), pool.load(session.id()), sameInstance(session));
        }
    }
}
