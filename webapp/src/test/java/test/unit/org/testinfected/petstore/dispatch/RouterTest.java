package test.unit.org.testinfected.petstore.dispatch;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Renderer;
import org.testinfected.petstore.dispatch.Destination;
import org.testinfected.petstore.dispatch.Route;
import org.testinfected.petstore.dispatch.Router;
import org.testinfected.petstore.pipeline.Dispatcher;

import java.io.IOException;

@RunWith(JMock.class)
public class RouterTest {

    Mockery context = new JUnit4Mockery();
    Destination defaultDestination = context.mock(Destination.class);
    Router router = new Router(defaultDestination);

    Request request = context.mock(Request.class);
    Response response = context.mock(Response.class);
    Router dummyRouter = null;
    Renderer dummyRenderer = null;
    Dispatcher dispatcher = new Dispatcher(dummyRouter, dummyRenderer);

    Route preferredRoute = context.mock(Route.class, "preferred route");
    Route alternateRoute = context.mock(Route.class, "alternate route");

    @Test public void
    routesToDefaultDestinationWhenNoAppropriateRouteExists() throws Exception {
        context.checking(new Expectations() {{
            oneOf(defaultDestination).handle(with(same(request)), with(same(response)), with(same(dispatcher)));
        }});
        router.add(new DeadEnd());

        router.dispatch(request, response, dispatcher);
    }

    @Test public void
    dispatchesToFirstRouteConnectingToDestination() throws Exception {
        context.checking(new Expectations() {{
            allowing(preferredRoute).connects(with(same(request))); will(returnValue(true));
            allowing(alternateRoute).connects(with(same(request))); will(returnValue(true));

            oneOf(preferredRoute).dispatch(with(same(request)), with(same(response)), with(same(dispatcher)));
        }});
        router.add(preferredRoute);
        router.add(alternateRoute);

        router.dispatch(request, response, dispatcher);
    }

    public static class DeadEnd implements Route {
        public boolean connects(Request request) {
            return false;
        }

        public void dispatch(Request request, Response response, Dispatcher dispatcher) throws IOException {
        }
    }
}
