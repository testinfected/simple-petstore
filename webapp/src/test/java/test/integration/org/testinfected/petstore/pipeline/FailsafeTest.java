package test.integration.org.testinfected.petstore.pipeline;

import com.gargoylesoftware.htmlunit.WebResponse;
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
import test.support.org.testinfected.petstore.web.WebRequestBuilder;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.org.testinfected.petstore.web.HasContent.hasContent;
import static test.support.org.testinfected.petstore.web.HasHeaderWithValue.hasHeader;
import static test.support.org.testinfected.petstore.web.HasStatusCode.hasStatusCode;
import static test.support.org.testinfected.petstore.web.OfflineContext.offlineContext;
import static test.support.org.testinfected.petstore.web.WebRequestBuilder.aRequest;

@RunWith(JMock.class)
public class FailsafeTest {

    Mockery context = new JUnit4Mockery();
    FailureReporter failureReporter = context.mock(FailureReporter.class);

    Failsafe failsafe = new Failsafe(offlineContext().renderer());
    Exception error = new Exception("Crashed!");

    Application app = new MiddlewareStack() {{
        use(failsafe);
        run(crashesWith(error));
    }};

    int SERVER_LISTENING_PORT = 9999;
    Server server = new Server(SERVER_LISTENING_PORT);
    WebRequestBuilder request = aRequest().onPort(SERVER_LISTENING_PORT);

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
        WebResponse response = request.send();

        assertThat("response", response, hasStatusCode(500));
        assertThat("response", response, hasHeader("Content-Type", containsString("text/html")));
        assertThat("response", response, hasContent(containsString("Crashed!")));
    }

    @Test public void
    notifiesFailureReporterWhenInternalErrorOccurs() throws IOException {
        failsafe.reportErrorsTo(failureReporter);
        expectFailureReport();

        request.send();
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