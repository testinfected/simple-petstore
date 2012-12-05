package test.unit.org.testinfected.support.routing;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.support.Application;
import org.testinfected.support.middlewares.NotFound;
import org.testinfected.support.routing.Route;
import org.testinfected.support.middlewares.Routes;
import org.testinfected.support.Matcher;
import test.support.org.testinfected.support.web.MockSimpleRequest;
import test.support.org.testinfected.support.web.MockSimpleResponse;
import org.testinfected.support.matchers.Nothing;

import static org.testinfected.support.matchers.Matchers.anyRequest;
import static test.support.org.testinfected.support.web.MockSimpleRequest.aRequest;
import static test.support.org.testinfected.support.web.MockSimpleResponse.aResponse;

@RunWith(JMock.class)
public class RoutesTest {

    Routes routes = new Routes(NotFound.notFound());

    MockSimpleRequest request = aRequest();
    MockSimpleResponse response = aResponse();

    Mockery context = new JUnit4Mockery();
    Application wrongApp = context.mock(Application.class, "wrong app");
    Application preferredApp = context.mock(Application.class, "preferred app");
    Application alternateApp = context.mock(Application.class, "alternate app");
    Application fallbackApp = context.mock(Application.class, "fallback app");

    @Test public void
    routesToDefaultApplicationWhenNoRouteMatches() throws Exception {
        routes.defaultsTo(fallbackApp).add(new StaticRoute(noRequest(), wrongApp));

        context.checking(new Expectations() {{
            never(wrongApp);
            oneOf(fallbackApp).handle(with(same(request)), with(same(response)));
        }});

        routes.handle(request, response);
    }

    @Test public void
    dispatchesToFirstRouteThatMatches() throws Exception {
        context.checking(new Expectations() {{
            oneOf(preferredApp).handle(with(same(request)), with(same(response)));
            never(alternateApp);
        }});
        routes.add(new StaticRoute(anyRequest(), preferredApp));
        routes.add(new StaticRoute(anyRequest(), alternateApp));

        routes.handle(request, response);
    }

    private Matcher<Request> noRequest() {
        return new Nothing<Request>();
    }

    private class StaticRoute implements Route {
        private final Matcher<Request> requestMatcher;
        private final Application app;

        public StaticRoute(Matcher<Request> requestMatcher, Application app) {
            this.requestMatcher = requestMatcher;
            this.app = app;
        }

        public void handle(org.testinfected.support.Request request, org.testinfected.support.Response response) throws Exception {
        }

        public boolean matches(org.testinfected.support.Request actual) {
            return matches(actual.unwrap(org.simpleframework.http.Request.class));
        }

        public void handle(Request request, Response response) throws Exception {
            app.handle(request, response);
        }

        public boolean matches(Request actual) {
            return requestMatcher.matches(actual);
        }
    }
}
