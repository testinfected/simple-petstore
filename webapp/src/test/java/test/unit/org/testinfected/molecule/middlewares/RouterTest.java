package test.unit.org.testinfected.molecule.middlewares;

import org.junit.Test;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.matchers.Anything;
import org.testinfected.molecule.matchers.Nothing;
import org.testinfected.molecule.middlewares.Router;
import org.testinfected.molecule.routing.Route;
import org.testinfected.molecule.util.Matcher;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

public class RouterTest {

    Router router = new Router();
    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Test public void
    routesToDefaultWhenNoRouteMatches() throws Exception {
        router.defaultsTo(route("default")).add(new StaticRoute(none(), route("other")));
        router.handle(request, response);
        assertRoutedTo("default");
    }

    @Test public void
    dispatchesToFirstRouteThatMatches() throws Exception {
        router.add(new StaticRoute(all(), route("preferred")));
        router.add(new StaticRoute(all(), route("alternate")));
        router.handle(request, response);
        assertRoutedTo("preferred");
    }

    private void assertRoutedTo(String route) {
        assertThat("route", response.body(), equalTo(route));
    }

    private Application route(final String name) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body(name);
            }
        };
    }

    public static Matcher<Request> all() {
        return new Anything<Request>();
    }

    public static Matcher<Request> none() {
        return new Nothing<Request>();
    }

    private class StaticRoute implements Route {
        private final Matcher<Request> requestMatcher;
        private final Application app;

        public StaticRoute(Matcher<Request> requestMatcher, Application app) {
            this.requestMatcher = requestMatcher;
            this.app = app;
        }

        public void handle(Request request, Response response) throws Exception {
            app.handle(request, response);
        }

        public boolean matches(Request actual) {
            return requestMatcher.matches(actual);
        }
    }
}
