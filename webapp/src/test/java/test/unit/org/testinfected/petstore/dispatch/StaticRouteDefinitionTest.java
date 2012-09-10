package test.unit.org.testinfected.petstore.dispatch;

import org.junit.Test;
import org.simpleframework.http.Request;
import org.testinfected.petstore.routing.StaticRouteDefinition;
import org.testinfected.petstore.util.HttpMethod;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.petstore.routing.StaticRouteDefinition.staticRoute;
import static test.support.org.testinfected.petstore.web.MockRequest.POST;

public class StaticRouteDefinitionTest {

    Request request = POST("/path/to/resource");

    @Test public void
    matchesAllRequestsByDefault() throws Exception {
        StaticRouteDefinition route = staticRoute();
        assertThat("no match", route.matches(request));
    }

    @Test public void
    matchesRequestWhenRequestPathStartsWithRoutePath() throws Exception {
        StaticRouteDefinition route = staticRoute().map("/path");
        assertThat("no match", route.matches(request));
    }

    @Test public void
    matchesRequestOnlyWhenHttpMethodMatches() throws Exception {
        assertThat("no match", staticRoute().via(HttpMethod.POST).matches(request));
        assertThat("match", !staticRoute().via(HttpMethod.DELETE).matches(request));
    }
}
