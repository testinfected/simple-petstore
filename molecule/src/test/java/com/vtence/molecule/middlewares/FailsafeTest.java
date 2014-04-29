package com.vtence.molecule.middlewares;

import com.vtence.molecule.Application;
import com.vtence.molecule.http.HttpStatus;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class FailsafeTest {
    Failsafe failsafe = new Failsafe();

    String errorMessage = "An error occurred!";
    Error error = new Error(errorMessage) {{
        setStackTrace(new StackTraceElement[] {
                      new StackTraceElement("stack", "trace", "line", 1),
                      new StackTraceElement("stack", "trace", "line", 2)
        });
    }};

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    @Before public void
    handleRequest() throws Exception {
        failsafe.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                throw error;
            }
        });
        failsafe.handle(request, response);
    }

    @Test public void
    setsStatusToInternalServerError() {
        response.assertStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test public void
    rendersErrorTemplate() {
        response.assertBody(containsString(errorMessage));
        response.assertBody(containsString("stack.trace(line:1)"));
        response.assertBody(containsString("stack.trace(line:2)"));
    }

    @Test public void
    respondsWithHtmlContentUtf8Encoded() {
        response.assertHeader("Content-Type", equalTo("text/html; charset=utf-8"));
    }
}