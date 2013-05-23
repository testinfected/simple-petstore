package test.unit.org.testinfected.molecule.middlewares;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.matchers.Anything;
import org.testinfected.molecule.matchers.Nothing;
import org.testinfected.molecule.middlewares.NotFound;
import org.testinfected.molecule.middlewares.Router;
import org.testinfected.molecule.routing.Route;
import org.testinfected.molecule.util.Matcher;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

@RunWith(JMock.class)
public class RouterTest {

    Router router = new Router(NotFound.notFound());

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    Mockery context = new JUnit4Mockery();
    Application wrongApp = context.mock(Application.class, "wrong app");
    Application preferredApp = context.mock(Application.class, "preferred app");
    Application alternateApp = context.mock(Application.class, "alternate app");
    Application fallbackApp = context.mock(Application.class, "fallback app");

    @Test public void
    routesToDefaultApplicationWhenNoRouteMatches() throws Exception {
        router.defaultsTo(fallbackApp).add(new StaticRoute(none(), wrongApp));

        context.checking(new Expectations() {{
            never(wrongApp);
            oneOf(fallbackApp).handle(request, response);
        }});

        router.handle(request, response);
    }

    @Test public void
    dispatchesToFirstRouteThatMatches() throws Exception {
        router.add(new StaticRoute(all(), preferredApp));
        router.add(new StaticRoute(all(), alternateApp));

        context.checking(new Expectations() {{
            oneOf(preferredApp).handle(request, response);
            never(alternateApp);
        }});

        router.handle(request, response);
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
