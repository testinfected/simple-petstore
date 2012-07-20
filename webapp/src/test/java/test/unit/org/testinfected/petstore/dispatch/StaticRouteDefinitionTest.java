package test.unit.org.testinfected.petstore.dispatch;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleframework.http.Request;
import org.simpleframework.http.parse.PathParser;
import org.testinfected.petstore.dispatch.StaticRoute;
import org.testinfected.petstore.util.HttpMethod;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.petstore.dispatch.StaticRouteDefinition.staticRoute;

@RunWith(JMock.class)
public class StaticRouteDefinitionTest {

    Mockery context = new JUnit4Mockery();
    Request request = context.mock(Request.class);

    @Before public void
    setupRequest() {
        context.checking(new Expectations() {{
            allowing(request).getPath(); will(returnValue(new PathParser("/path/to/resource")));
            allowing(request).getMethod(); will(returnValue("POST"));
        }});
    }

    @Test public void
    matchesAllRequestsByDefault() throws Exception {
        StaticRoute route = staticRoute().toRoute();
        assertThat("no match", route.matches(request));
    }

    @Test public void
    matchesRequestWhenRequestPathStartsWithRoutePath() throws Exception {
        StaticRoute route = staticRoute().map("/path").toRoute();
        assertThat("no match", route.matches(request));
    }

    @Test public void
    matchesRequestOnlyWhenHttpMethodMatches() throws Exception {
        assertThat("no match", staticRoute().via(HttpMethod.post).toRoute().matches(request));
        assertThat("match", !staticRoute().via(HttpMethod.delete).toRoute().matches(request));
    }
}
