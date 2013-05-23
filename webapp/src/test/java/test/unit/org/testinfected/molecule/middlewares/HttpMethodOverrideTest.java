package test.unit.org.testinfected.molecule.middlewares;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.HttpMethod;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.middlewares.HttpMethodOverride;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import static org.testinfected.molecule.HttpMethod.DELETE;
import static org.testinfected.molecule.HttpMethod.GET;
import static org.testinfected.molecule.HttpMethod.POST;
import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

@RunWith(JMock.class)
public class HttpMethodOverrideTest {
    Mockery context = new JUnit4Mockery();
    Application successor = context.mock(Application.class, "successor");

    HttpMethodOverride methodOverride = new HttpMethodOverride();

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Before public void
    chainWithSuccessor()  {
        methodOverride.connectTo(successor);
    }

    @Test public void
    doesNotAffectGetMethods() throws Exception {
        request.addParameter("_method", "delete");

        context.checking(new Expectations() {{
            oneOf(successor).handle(with(requestWithMethod(GET)), with(response));
        }});

        methodOverride.handle(request.withMethod(GET), response);
    }

    @Test public void
    leavesMethodUnchangedWhenOverrideParameterAbsent() throws Exception {
        context.checking(new Expectations() {{
            oneOf(successor).handle(with(requestWithMethod(POST)), with(response));
        }});

        methodOverride.handle(request.withMethod(POST), response);
    }

    @Test public void
    changesPostMethodsAccordingToOverrideParameter() throws Exception {
        request.addParameter("_method", "delete");

        context.checking(new Expectations() {{
            oneOf(successor).handle(with(requestWithMethod(DELETE)), with(response));
        }});

        methodOverride.handle(request.withMethod(POST), response);
    }

    @Test public void
    leavesMethodUnchangedIfMethodIsNotSupported() throws Exception {
        request.withParameter("_method", "unsupported");

        context.checking(new Expectations() {{
            oneOf(successor).handle(with(requestWithMethod(POST)), with(response));
        }});

        methodOverride.handle(request.withMethod(POST), response);
    }

    private Matcher<Request> requestWithMethod(HttpMethod method) {
        return new FeatureMatcher<Request, HttpMethod>(Matchers.equalTo(method), "a request with method", "method") {
            protected HttpMethod featureValueOf(Request request) {
                return request.method();
            }
        };
    }
}