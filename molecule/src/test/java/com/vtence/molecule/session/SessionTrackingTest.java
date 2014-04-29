package com.vtence.molecule.session;

import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import com.vtence.molecule.support.MockSession;
import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.Session;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class SessionTrackingTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    SessionTracker tracker = context.mock(SessionTracker.class);

    Request request = new MockRequest();
    Response response = new MockResponse();
    Session client = new MockSession();

    Session none = null;
    States session = context.states("session").startsAs("not opened");

    SessionTracking tracking = new SessionTracking(tracker, response);

    @Test public void
    acquiresSessionFromTracker() {
        track(client);
        assertThat("session", tracking.openSession(request, false), sameInstance(client));
    }

    @Test public void
    cachesAlreadyOpenedSession() {
        track(client);
        tracking.openSession(request, false);

        context.checking(new Expectations() {{
            never(tracker).acquireSession(request, response); when(session.is("opened"));
        }});

        tracking.openSession(request, false);
    }

    @Test public void
    createsSessionOnDemand() {
        track(none);
        assertThat("session(false)", tracking.openSession(request, false), nullValue());

        context.checking(new Expectations() {{
            oneOf(tracker).openSession(request, response); will(returnValue(client));
        }});
        assertThat("session(true)", tracking.openSession(request, true), sameInstance(client));
        assertThat("session(true)", tracking.openSession(request, false), sameInstance(client));
    }

    private void track(final Session client) {
        context.checking(new Expectations() {{
            allowing(tracker).acquireSession(request, response); will(returnValue(client)); when(session.is("not opened"));
                then(session.is(client != null ? "opened" : "not opened"));
        }});
    }
}
