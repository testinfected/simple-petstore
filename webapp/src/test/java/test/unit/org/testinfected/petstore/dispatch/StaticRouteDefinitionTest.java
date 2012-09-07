package test.unit.org.testinfected.petstore.dispatch;

import org.junit.Test;
import org.simpleframework.http.Request;
import org.testinfected.petstore.routing.StaticRouteDefinition;
import org.testinfected.petstore.util.HttpMethod;
import test.support.org.testinfected.petstore.web.MockRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.petstore.routing.StaticRouteDefinition.staticRoute;

public class StaticRouteDefinitionTest {

    Request request = MockRequest.post("/path/to/resource");

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
        assertThat("no match", staticRoute().via(HttpMethod.post).matches(request));
        assertThat("match", !staticRoute().via(HttpMethod.delete).matches(request));
    }
}
