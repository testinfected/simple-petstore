package test.unit.org.testinfected.molecule.session;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Test;
import org.testinfected.molecule.Session;
import org.testinfected.molecule.session.SessionHash;
import org.testinfected.molecule.session.SessionSweeping;
import test.support.org.testinfected.molecule.unit.BrokenClock;
import test.support.org.testinfected.molecule.unit.DateBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static test.support.org.testinfected.molecule.unit.DateBuilder.aDate;

public class SessionSweepingTest {

    int timeout = 30;
    DateBuilder creationTime = aDate().atTime(15, 30, 00);
    DateBuilder currentTime = creationTime.but().atTime(15, 31, 00);
    SessionSweeping sweeping = new SessionSweeping(BrokenClock.stoppedAt(currentTime.build()));

    Collection<Session> pool = new ArrayList<Session>();

    @Test public void
    leavesValidSessionsInPool() throws Exception {
        Collection<Session> valid = addSessionsToPool("valid", 10);
        sweeping.perform(pool.iterator());
        assertThat("valid sessions", pool, containsSessions(valid));
    }

    @Test public void
    removesInvalidSessionsFromPool() throws Exception {
        Collection<Session> valid = addSessionsToPool("valid", 10);
        invalidate(addSessionsToPool("invalid", 10));

        sweeping.perform(pool.iterator());
        assertThat("valid sessions", pool, containsSessions(valid));
    }

    @Test public void
    invalidatesAndRemovesExpiredSessionsFromPool() throws Exception {
        Collection<Session> valid = addSessionsToPool("valid", 10);
        Collection<Session> expired = expire(addSessionsToPool("expired", 10));

        sweeping.perform(pool.iterator());
        assertThat("valid sessions", pool, containsSessions(valid));

        for (Session session : expired) {
            assertThat(session.id() + " invalid?", session.invalid(), equalTo(true));
        }
    }

    @SuppressWarnings("unchecked")
    private Matcher<? super Collection<Session>> containsSessions(Collection<Session> items) {
        List<Matcher<? super Session>> matchers = new ArrayList<Matcher<? super Session>>();
        for (Session item : items) {
            matchers.add(equalTo(item));
        }

        return IsIterableContainingInOrder.contains(matchers);
    }

    private Collection<Session> invalidate(Collection<Session> sessions) {
        for (Session session : sessions) {
            session.invalidate();
        }
        return sessions;
    }

    private Collection<Session> expire(Collection<Session> sessions) {
        for (Session session : sessions) {
            session.timeout(timeout);
        }
        return sessions;
    }

    private Collection<Session> addSessionsToPool(String name, int count) {
        Collection<Session> sessions = new ArrayList<Session>();
        for (int i = 1; i <= count; i++) {
            sessions.add(createSession(name + "-" + i));
        }
        pool.addAll(sessions);
        return sessions;
    }

    private SessionHash createSession(String name) {
        return new SessionHash(name, creationTime.build());
    }
}
