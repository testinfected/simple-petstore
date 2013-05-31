package test.unit.org.testinfected.molecule.middlewares;

import org.junit.Before;
import org.junit.Test;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.HttpStatus;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.middlewares.Failsafe;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import static org.hamcrest.CoreMatchers.containsString;
import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

public class FailsafeTest {
    Failsafe failsafe = new Failsafe();

    String errorMessage = "An internal error occurred!";
    Exception error = new Exception(errorMessage) {{
        setStackTrace(new StackTraceElement[]{
                new StackTraceElement("stack", "trace", "line", 1),
                new StackTraceElement("stack", "trace", "line", 2)
        });
    }};

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Before public void
    handleRequest() throws Exception {
        failsafe.connectTo(crashWith(error));
        failsafe.handle(request, response);
    }

    @Test public void
    setStatusToInternalServerError() {
        response.assertStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test public void
    rendersErrorTemplate() {
        response.assertBody(containsString(errorMessage));
        response.assertBody(containsString("stack.trace(line:1)"));
        response.assertBody(containsString("stack.trace(line:2)"));
    }

    @Test public void
    setsResponseContentTypeToHtml() {
        response.assertHeader("Content-Type", containsString("text/html"));
    }

    private Application crashWith(final Exception error) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                throw error;
            }
        };
    }
}