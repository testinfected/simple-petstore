package test.unit.org.testinfected.molecule.session;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.molecule.Session;
import org.testinfected.molecule.session.SessionListener;
import org.testinfected.molecule.session.SessionPool;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class SessionPoolTest {

    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    SessionListener listener = context.mock(SessionListener.class);
    SessionPool pool = new SessionPool(listener);

    @Test public void
    isInitiallyEmpty() throws Exception {
        context.checking(new Expectations() {{
            never(listener);
        }});
        Session session = pool.load("session");
        assertThat("session", session, nullValue());
    }

    @Test public void
    createsEmptySessions() throws Exception {
        ignoreNotifications();
        Session session = pool.create("session");
        assertThat("session", session, notNullValue());
        assertThat("session keys", session.keys(), emptyIterable());
    }

    @Test public void
    notifiesWhenSessionsAreCreated() throws Exception {
        context.checking(new Expectations() {{
            oneOf(listener).sessionCreated(with(sessionWithId("key")));
        }});
        pool.create("key");
    }

    @Test public void
    notifiesWhenSessionsAreAccessed() throws Exception {
        addToPool("key");

        context.checking(new Expectations() {{
            oneOf(listener).sessionAccessed(with(sessionWithId("key")));
        }});
        pool.load("key");
    }

    @Test public void
    storesAndRetrievesMultipleSessions() throws Exception {
        ignoreNotifications();
        Session session1 = pool.create("session1");
        Session session2 = pool.create("session2");
        Session session3 = pool.create("session3");

        assertThat("session 1", pool.load("session1"), equalTo(session1));
        assertThat("session 2", pool.load("session2"), equalTo(session2));
        assertThat("session 3", pool.load("session3"), equalTo(session3));
    }

    private void addToPool(String... keys) {
        ignoreCreation();
        for (String key : keys) {
            pool.create(key);
        }
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
}
