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
    Exception error = new Exception("Crashed!");

    Application app = new MiddlewareStack() {{
        use(failsafe);
        run(crashesWith(error));
    }};

    Server server = new Server(9999);
    HttpRequest request = aRequest().to(server);

    @Before public void
    startServer() throws IOException {
        server.run(app);
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    rendersErrorTemplateWhenInternalErrorOccurs() throws IOException {
        failsafe.setErrorTemplate("error");
        context.checking(new Expectations() {{
            oneOf(renderer).render(with("error"), with(same(error))); will(returnValue("Crashed!"));
        }});

        HttpResponse response = request.get("/crash");
        response.assertHasStatusCode(500);
        response.assertHasHeader("Content-Type", containsString("text/html"));
        response.assertHasContent(containsString("Crashed!"));
    }

    @Test public void
    notifiesFailureReporterWhenInternalErrorOccurs() throws IOException {
        failsafe.reportErrorsTo(failureReporter);
        context.checking(new Expectations() {{
            allowing(renderer).render(with(any(String.class)), with(any(Object.class)));
            oneOf(failureReporter).internalErrorOccurred(with(same(error)));
        }});
        request.get("/crash");
        context.assertIsSatisfied();
    }

    private Application crashesWith(final Exception error) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                throw error;
            }
        };
    }
}