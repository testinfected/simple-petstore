package com.vtence.molecule.routing;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import com.vtence.molecule.Application;
import com.vtence.molecule.HttpMethod;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;

import static com.vtence.molecule.support.MockRequest.GET;
import static com.vtence.molecule.support.MockRequest.POST;
import static com.vtence.molecule.support.MockRequest.aRequest;
import static com.vtence.molecule.support.MockResponse.aResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DynamicRouteDefinitionTest {

    DynamicRouteDefinition definition = new DynamicRouteDefinition();
    Mockery context = new JUnit4Mockery();
    Application app = context.mock(Application.class);

    @Test(expected = IllegalStateException.class) public void
    isInvalidIfNoPathWasSpecified() throws Exception {
        definition.toRoute();
    }

    @Test public void
    isValidWithAPath() throws Exception {
        definition.map("/resource").toRoute();
    }

    @Test public void
    routesRequestWhenPathMatchesGivenPattern() throws Exception {
        Route route = definition.map("/resource/:id").toRoute();
        assertThat("no match", route.matches(GET("/resource/1")));
        assertThat("match", !route.matches(GET("/other/1")));
    }

    @Test public void
    routesRequestWhenHttpMethodMatches() throws Exception {
        Route route = definition.map("/resource").via(HttpMethod.POST).toRoute();
        assertThat("no match", route.matches(POST("/resource")));
        assertThat("match", !route.matches(GET("/resource")));
    }

    @Test public void
    routesRequestWithParameters() throws Exception {
        Route route = definition.map("/resource").to(app).toRoute();
        context.checking(new Expectations() {{
            oneOf(app).handle(with(hasParameter("number", "100")), with(any(Response.class)));
        }});
        route.handle(aRequest().withPath("/resource").withParameter("number", "100"), aResponse());
    }

    @Test public void
    pathParametersShadowRequestParametersWithSameName() throws Exception {
        Route route = definition.map("/resource/:id").to(app).toRoute();
        context.checking(new Expectations() {{
            oneOf(app).handle(with(hasParameter("id", "1")), with(any(Response.class)));
        }});
        route.handle(aRequest().withPath("/resource/1").withParameter("id", "not 1"), aResponse());
    }

    private Matcher<Request> hasParameter(final String name, String value) {
        return new FeatureMatcher<Request, String>(equalTo(value), "request with parameter " + name, name) {
            protected String featureValueOf(Request actual) {
                return actual.parameter(name);
            }
        };
    }
}
