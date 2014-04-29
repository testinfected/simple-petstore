package com.vtence.molecule.middlewares;

import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.junit.Before;
import org.junit.Test;
import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;

import static com.vtence.molecule.support.MockRequest.aRequest;
import static com.vtence.molecule.support.MockResponse.aResponse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static com.vtence.molecule.HttpMethod.GET;
import static com.vtence.molecule.HttpMethod.POST;

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