package test.integration.org.testinfected.support.middlewares;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.support.*;
import org.testinfected.support.middlewares.Failsafe;
import test.support.org.testinfected.support.web.HttpRequest;
import test.support.org.testinfected.support.web.HttpResponse;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.containsString;
import static test.support.org.testinfected.support.web.HttpRequest.aRequest;

@RunWith(JMock.class)
public class FailsafeTest {

    Mockery context = new JUnit4Mockery();
    FailureReporter failureReporter = context.mock(FailureReporter.class);

    Failsafe failsafe = new Failsafe();

    String errorMessage = "An internal error occurred!";
    Exception error = new Exception(errorMessage);
    {
        error.setStackTrace(new StackTraceElement[] {
                new StackTraceElement("stack", "trace", "line", 1),
                new StackTraceElement("stack", "trace", "line", 2)
        });
    }
    Server server = new Server(9999);
    HttpRequest request = aRequest().to(server);
    HttpResponse response;

    @Before public void
    sendRequestToServer() throws IOException {
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

    @Test public void
    notifiesFailureReporter() throws IOException {
        failsafe.reportErrorsTo(failureReporter);
        context.checking(new Expectations() {{
            oneOf(failureReporter).internalErrorOccurred(with(same(error)));
        }});
        request.send();
    }

    private Application failWith(final Exception error) {
        return new Application() {
            public void handle(org.simpleframework.http.Request request, org.simpleframework.http.Response response) throws Exception {
                handle(new SimpleRequest(request), new SimpleResponse(response, null, Charset.defaultCharset()));
            }

            public void handle(Request request, Response response) throws Exception {
                throw error;
            }
        };
    }
}