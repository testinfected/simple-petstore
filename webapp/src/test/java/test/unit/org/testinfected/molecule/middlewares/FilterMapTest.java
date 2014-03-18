package test.unit.org.testinfected.molecule.middlewares;

import org.junit.Before;
import org.junit.Test;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.matchers.Matchers;
import org.testinfected.molecule.middlewares.AbstractMiddleware;
import org.testinfected.molecule.middlewares.FilterMap;
import org.testinfected.molecule.util.Matcher;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

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
