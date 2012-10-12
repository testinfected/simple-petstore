package test.unit.org.testinfected.petstore.dispatch;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleframework.http.Request;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.routing.Routes;
import org.testinfected.petstore.routing.StaticRoute;
import org.testinfected.petstore.util.Matcher;
import test.support.org.testinfected.petstore.web.MockRequest;
import test.support.org.testinfected.petstore.web.MockResponse;
import test.support.org.testinfected.petstore.web.Nothing;

import static org.testinfected.petstore.util.Matchers.anyRequest;

@RunWith(JMock.class)
public class RoutesTest {

    Routes routes = new Routes();

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    Mockery context = new JUnit4Mockery();
    Application wrongApp = context.mock(Application.class, "wrong app");
    Application preferredApp = context.mock(Application.class, "preferred app");
    Application alternateApp = context.mock(Application.class, "alternate app");
    Application fallbackApp = context.mock(Application.class, "fallback app");

    @Test public void
    routesToDefaultApplicationWhenNoRouteMatches() throws Exception {
        routes.defaultsTo(fallbackApp).add(new StaticRoute(never(), wrongApp));

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

    private Matcher<Request> never() {
        return new Nothing<Request>();
    }
}
