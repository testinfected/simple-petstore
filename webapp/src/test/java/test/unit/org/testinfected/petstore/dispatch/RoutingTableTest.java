package test.unit.org.testinfected.petstore.dispatch;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleframework.http.Request;
import org.testinfected.petstore.dispatch.Route;
import org.testinfected.petstore.dispatch.Router;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JMock.class)
public class RoutingTableTest {

    Mockery context = new JUnit4Mockery();
    Router.RoutingTable routingTable = new Router.RoutingTable();

    Request request = context.mock(Request.class);

    Route inappropriateRoute = context.mock(Route.class, "inappropriate route");
    Route preferredRoute = context.mock(Route.class, "preferred route");
    Route alternateRoute = context.mock(Route.class, "alternate route");
    Route defaultRoute = context.mock(Route.class, "default route");

    @Test public void
    routesToDefaultWhenNoAppropriateRouteExists() throws Exception {
        context.checking(new Expectations() {{
            allowing(inappropriateRoute).matches(with(same(request))); will(returnValue(false));
        }});
        routingTable.add(inappropriateRoute);
        routingTable.setDefaultRoute(defaultRoute);
        assertThat("route", routingTable.locateRoute(request), sameInstance(defaultRoute));
    }

    @Test public void
    dispatchesToFirstAppropriateRoute() throws Exception {
        context.checking(new Expectations() {{
            allowing(preferredRoute).matches(with(same(request))); will(returnValue(true));
            allowing(alternateRoute).matches(with(same(request))); will(returnValue(true));
        }});
        routingTable.add(preferredRoute);
        routingTable.add(alternateRoute);

        assertThat("route", routingTable.locateRoute(request), sameInstance(preferredRoute));
    }
}
