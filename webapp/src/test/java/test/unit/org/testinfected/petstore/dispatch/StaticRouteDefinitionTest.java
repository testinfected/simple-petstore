package test.unit.org.testinfected.petstore.dispatch;

import org.junit.Test;
import org.testinfected.petstore.dispatch.Dispatch;
import org.testinfected.petstore.dispatch.StaticRoute;
import org.testinfected.petstore.util.HttpMethod;
import test.support.org.testinfected.petstore.web.MockRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.petstore.dispatch.StaticRouteDefinition.staticRoute;

public class StaticRouteDefinitionTest {

    Dispatch.Request request = new MockRequest("POST", "/path/to/resource");

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
