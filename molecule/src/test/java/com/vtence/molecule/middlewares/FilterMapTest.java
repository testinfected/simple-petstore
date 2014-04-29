package com.vtence.molecule.middlewares;

import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.junit.Before;
import org.junit.Test;
import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.matchers.Matchers;
import com.vtence.molecule.util.Matcher;

import static com.vtence.molecule.support.MockRequest.aRequest;
import static com.vtence.molecule.support.MockResponse.aResponse;

public class FilterMapTest {

    FilterMap filters = new FilterMap();

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Before public void
    stubApplication() {
        filters.connectTo(write("content"));
    }

    @Test public void
    immediatelyForwardsRequestWhenNoFilterIsRegistered() throws Exception {
        filters.handle(request, response);
        response.assertBody("content");
    }

    @Test public void
    runsRequestThroughMatchingFilter() throws Exception {
        filters.map(none(), filter("wrong"));
        filters.map(all(), filter("right"));

        filters.handle(request, response);
        response.assertBody("right content");
    }

    @Test public void
    forwardsRequestIfNoFilterMatches() throws Exception {
        filters.map(none(), filter("wrong"));
        filters.handle(request, response);
        response.assertBody("content");
    }

    @Test public void
    matchesOnPathPrefix() throws Exception {
        request.withPath("/filtered/path");
        filters.map("/filtered", filter("filtered"));

        filters.handle(request, response);
        response.assertBody("filtered content");
    }

    @Test public void
    appliesLastRegisteredOfMatchingFilters() throws Exception {
        filters.map(all(), filter("old"));
        filters.map(all(), filter("new"));

        filters.handle(request, response);
        response.assertBody("new content");
    }

    private Application write(final String text) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body(text);
            }
        };
    }

    private Matcher<Request> all() {
        return Matchers.anything();
    }

    private Matcher<Request> none() {
        return Matchers.nothing();
    }

    private AbstractMiddleware filter(final String name) {
        return new AbstractMiddleware() {
            public void handle(Request request, Response response) throws Exception {
                response.body(name + " ");
                forward(request, response);
            }
        };
    }
}
