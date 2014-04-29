package com.vtence.molecule.routing;

import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import com.vtence.molecule.Application;
import com.vtence.molecule.http.HttpMethod;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static com.vtence.molecule.support.MockRequest.*;
import static org.hamcrest.Matchers.is;

public class DynamicRouteDefinitionTest {

    DynamicRouteDefinition definition = new DynamicRouteDefinition();
    Mockery context = new JUnit4Mockery();
    Application app = context.mock(Application.class);
    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

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
    routesRequestWhenHttpMethodMatchesAny() throws Exception {
        Route route = definition.map("/resource").via(HttpMethod.POST, HttpMethod.PUT).toRoute();
        assertThat("1st matches", route.matches(POST("/resource")), is(true));
        assertThat("2nd matches", !route.matches(GET("/resource")), is(true));
        assertThat("none matches", !route.matches(GET("/resource")), is(true));
    }

    @Test public void
    routesRequestWithParameters() throws Exception {
        Route route = definition.map("/resource").to(app).toRoute();
        context.checking(new Expectations() {{
            oneOf(app).handle(with(hasParameter("number", "100")), with(any(Response.class)));
        }});
        route.handle(request.path("/resource").addParameter("number", "100"), response);
    }

    @Test public void
    pathParametersShadowRequestParametersWithSameName() throws Exception {
        Route route = definition.map("/resource/:id").to(app).toRoute();
        context.checking(new Expectations() {{
            oneOf(app).handle(with(hasParameter("id", "1")), with(any(Response.class)));
        }});
        route.handle(request.path("/resource/1").addParameter("id", "not 1"), response);
    }

    private Matcher<Request> hasParameter(final String name, String value) {
        return new FeatureMatcher<Request, String>(equalTo(value), "request with parameter " + name, name) {
            protected String featureValueOf(Request actual) {
                return actual.parameter(name);
            }
        };
    }
}
