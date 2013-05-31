package test.unit.org.testinfected.molecule.middlewares;

import org.junit.Before;
import org.junit.Test;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.middlewares.HttpMethodOverride;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.molecule.HttpMethod.GET;
import static org.testinfected.molecule.HttpMethod.POST;
import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

public class HttpMethodOverrideTest {

    HttpMethodOverride methodOverride = new HttpMethodOverride();

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Before public void
    echoHttpMethod()  {
        methodOverride.connectTo(echoMethodName());
    }

    @Test public void
    doesNotAffectGetMethods() throws Exception {
        request.addParameter("_method", "delete");
        methodOverride.handle(request.withMethod(GET), response);
        assertMethod("GET");
    }

    @Test public void
    leavesMethodUnchangedWhenOverrideParameterAbsent() throws Exception {
        methodOverride.handle(request.withMethod(POST), response);
        assertMethod("POST");
    }

    @Test public void
    changesPostMethodsAccordingToOverrideParameter() throws Exception {
        request.addParameter("_method", "delete");
        methodOverride.handle(request.withMethod(POST), response);
        assertMethod("DELETE");
    }

    @Test public void
    leavesMethodUnchangedIfMethodIsNotSupported() throws Exception {
        request.withParameter("_method", "unsupported");
        methodOverride.handle(request.withMethod(POST), response);
        assertMethod("POST");
    }

    private Application echoMethodName() {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body(request.method().name());
            }
        };
    }

    private void assertMethod(String method) {
        assertThat("method", response.body(), equalTo(method));
    }
}