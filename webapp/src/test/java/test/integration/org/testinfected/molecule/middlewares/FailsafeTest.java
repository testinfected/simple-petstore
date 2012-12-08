package test.integration.org.testinfected.molecule.middlewares;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.MiddlewareStack;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.Server;
import org.testinfected.molecule.middlewares.Failsafe;
import org.testinfected.molecule.simple.SimpleServer;
import test.support.org.testinfected.molecule.web.HttpRequest;
import test.support.org.testinfected.molecule.web.HttpResponse;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static test.support.org.testinfected.molecule.web.HttpRequest.aRequest;

// todo Consider rewriting as unit test now that we can mock requests and responses
public class FailsafeTest {

    Failsafe failsafe = new Failsafe();

    String errorMessage = "An internal error occurred!";
    Exception error = new Exception(errorMessage);

    Server server = new SimpleServer(9999);
    HttpRequest request = aRequest().to(server);
    HttpResponse response;

    @Before public void
    startServerAndSendRequest() throws IOException {
        error.setStackTrace(new StackTraceElement[] {
                new StackTraceElement("stack", "trace", "line", 1),
                new StackTraceElement("stack", "trace", "line", 2)
        });

        server.run(new MiddlewareStack() {{
            use(failsafe);
            run(failWith(error));
        }});
        response = request.send();
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    setStatusCodeTo500() {
        response.assertHasStatusCode(500);
    }

    @Test public void
    setStatusMessageToInternalServerError() {
        response.assertHasStatusMessage("Internal Server Error");
    }

    @Test public void
    rendersErrorTemplate() {
        response.assertHasContent(containsString(errorMessage));
        response.assertHasContent(containsString("stack.trace(line:1)"));
        response.assertHasContent(containsString("stack.trace(line:2)"));
    }

    @Test public void
    setsResponseContentTypeToHtml() {
        response.assertHasHeader("Content-Type", containsString("text/html"));
    }

    private Application failWith(final Exception error) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                throw error;
            }
        };
    }
}