package test.integration.org.testinfected.petstore.pipeline;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.FailureReporter;
import org.testinfected.petstore.Renderer;
import org.testinfected.petstore.Server;
import org.testinfected.petstore.pipeline.Failsafe;
import org.testinfected.petstore.pipeline.MiddlewareStack;
import test.support.org.testinfected.petstore.web.HttpRequest;
import test.support.org.testinfected.petstore.web.HttpResponse;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static test.support.org.testinfected.petstore.web.HttpRequest.aRequest;

@RunWith(JMock.class)
public class FailsafeTest {

    Mockery context = new JUnit4Mockery();
    Renderer renderer = context.mock(Renderer.class);
    FailureReporter failureReporter = context.mock(FailureReporter.class);

    Failsafe failsafe = new Failsafe(renderer);
    Exception error = new Exception("Error");

    Server server = new Server(9999);
    HttpRequest request = aRequest().to(server);
    HttpResponse response;

    @Before public void
    sendRequestToServer() throws IOException {
        server.run(new MiddlewareStack() {{
            use(failsafe);
            run(errorOccurs(error));
        }});
        context.checking(new Expectations() {{
            allowing(renderer).render(with("500"), with(same(error))); will(returnValue("Error"));
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
        response.assertHasContent(containsString("Error"));
    }

    @Test public void
    setsContentLengthHeader() throws IOException {
        response.assertHasHeader("Content-Length", "5");
    }

    @Test public void
    setsResponseContentTypeToHtml() {
        response.assertHasHeader("Content-Type", containsString("text/html"));
    }

    @Test public void
    ensuresResponseIsNotChunked() {
        response.assertHasNoHeader("Transfer-Encoding");
    }

    @Test public void
    notifiesFailureReporter() throws IOException {
        failsafe.reportErrorsTo(failureReporter);
        context.checking(new Expectations() {{
            oneOf(failureReporter).internalErrorOccurred(with(same(error)));
        }});
        request.send();
    }

    private Application errorOccurs(final Exception error) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                throw error;
            }
        };
    }
}