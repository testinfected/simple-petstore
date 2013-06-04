package test.unit.org.testinfected.molecule.session;

import org.junit.Test;
import org.testinfected.molecule.Session;
import org.testinfected.molecule.session.SessionPool;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class SessionPoolTest {

    SessionPool pool = new SessionPool();

    @Test public void
    isInitiallyEmpty() throws Exception {
        Session session = pool.load("session", false);
        assertThat("session", session, nullValue());
    }

    @Test public void
    createsEmptySessionsOnDemand() throws Exception {
        Session session = pool.load("session", true);
        assertThat("session", session, notNullValue());
        assertThat("session keys", session.keys(), emptyIterable());
    }

    @Test public void
    storesAndRetrievesMultipleSessions() throws Exception {
        Session session1 = pool.load("session1", true);
        Session session2 = pool.load("session2", true);
        Session session3 = pool.load("session3", true);

        assertThat("session 1", pool.load("session1", false), equalTo(session1));
        assertThat("session 2", pool.load("session2", false), equalTo(session2));
        assertThat("session 3", pool.load("session3", false), equalTo(session3));
    }
}
