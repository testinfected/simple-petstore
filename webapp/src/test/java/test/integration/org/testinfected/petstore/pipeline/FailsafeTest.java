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
import org.testinfected.petstore.Server;
import org.testinfected.petstore.pipeline.Failsafe;
import org.testinfected.petstore.pipeline.MiddlewareStack;
import test.support.org.testinfected.petstore.web.HttpResponse;
import test.support.org.testinfected.petstore.web.OfflineContext;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static test.support.org.testinfected.petstore.web.HttpRequest.get;
import static test.support.org.testinfected.petstore.web.OfflineContext.fromSystemProperties;

@RunWith(JMock.class)
public class FailsafeTest {

    Mockery context = new JUnit4Mockery();
    FailureReporter failureReporter = context.mock(FailureReporter.class);

    Failsafe failsafe = new Failsafe(fromSystemProperties().renderer());
    Exception error = new Exception("Crashed!");

    Application app = new MiddlewareStack() {{
        use(failsafe);
        run(crashesWith(error));
    }};

    Server server = new Server(OfflineContext.TEST_PORT);

    @Before public void
    startServer() throws IOException {
        server.run(app);
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    renders500WhenInternalErrorOccurs() throws IOException {
        HttpResponse response = get("/crash");

        response.assertHasStatusCode(500);
        response.assertHasHeader("Content-Type", containsString("text/html"));
        response.assertHasContent(containsString("Crashed!"));
    }

    @Test public void
    notifiesFailureReporterWhenInternalErrorOccurs() throws IOException {
        failsafe.reportErrorsTo(failureReporter);
        expectFailureReport();

        get("/crash");
        context.assertIsSatisfied();
    }

    private void expectFailureReport() {
        context.checking(new Expectations() {{
            oneOf(failureReporter).internalErrorOccurred(with(same(error)));
        }});
    }

    private Application crashesWith(final Exception error) {
        return new Application() {
            public void handle(Request request, Response response) throws Exception {
                throw error;
            }
        };
    }
}