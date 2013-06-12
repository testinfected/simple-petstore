package test.unit.org.testinfected.molecule.session;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.molecule.Session;
import org.testinfected.molecule.session.CookieTracker;
import org.testinfected.molecule.session.SessionIdentifierPolicy;
import org.testinfected.molecule.session.SessionStore;
import org.testinfected.molecule.session.SessionTracker;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;
import test.support.org.testinfected.molecule.unit.MockSession;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CookieTrackerTest {

    static final String SESSION_COOKIE = "SESSION-COOKIE";
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    SessionStore store = context.mock(SessionStore.class);
    SessionTracker tracker = new CookieTracker(store, new StubPolicy("new-session"), SESSION_COOKIE);

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    @Test public void
    noSessionIsInitiallyAssociatedToClient() {
        Session session = tracker.acquireSession(request, response);
        assertThat("session", session, nullValue());
    }

    @Test public void
    usesACookieToIdentifyClients() throws Exception {
        request.withCookie(SESSION_COOKIE, "client-session");

        final Session clientSession = new MockSession("client-session");
        context.checking(new Expectations() {{
            allowing(store).load("client-session"); will(returnValue(clientSession));
        }});

        Session acquired = tracker.acquireSession(request, response);
        assertThat("acquired session", acquired, equalTo(clientSession));
    }

    @Test public void
    generatesIdsForNewSessions() throws Exception {
        context.checking(new Expectations() {{
            oneOf(store).create("new-session");
            will(returnValue(new MockSession("new-session")));
        }});

        Session newSession = tracker.openSession(request, response);
        assertThat("session", newSession, notNullValue());
        assertThat("session id", newSession.id(), equalTo("new-session"));
    }

    @Test public void
    tracksClientAcrossRequestsWithASessionCookie() throws Exception {
        context.checking(new Expectations() {{
            allowing(store).create("new-session");
            will(returnValue(new MockSession("new-session")));
        }});

        tracker.openSession(request, response);
        response.assertCookie(SESSION_COOKIE, "new-session");
    }

    private class StubPolicy implements SessionIdentifierPolicy {
        private final String id;

        public StubPolicy(String id) {
            this.id = id;
        }

        public String generateId() {
            return id;
        }
    }
}
