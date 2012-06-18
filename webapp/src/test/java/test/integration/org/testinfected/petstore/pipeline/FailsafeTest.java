package test.integration.org.testinfected.petstore.pipeline;

import com.gargoylesoftware.htmlunit.WebClient;
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
import org.testinfected.petstore.FailureReporter;
import org.testinfected.petstore.Handler;
import org.testinfected.petstore.Server;
import org.testinfected.petstore.pipeline.Application;
import org.testinfected.petstore.pipeline.Failsafe;
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

    Application application = new Application() {{
        use(failsafe);
        run(crashesWith(error));
    }};

    int SERVER_LISTENS_ON = 9999;
    Server server = new Server(SERVER_LISTENS_ON);
    WebRequestBuilder request = aRequest().onPort(SERVER_LISTENS_ON);

    @Before public void
    startServer() throws IOException {
        client.setTimeout(5000);
        server.run(application);
    }

    @After public void
    stopServer() throws Exception {
        server.stop();
    }

    @Test public void
    renders500WhenInternalErrorOccurs() throws IOException {
        send(request);

        assertThat("response", response, hasStatusCode(500));
        assertThat("response", response, hasHeader("Content-Type", containsString("text/html")));
        assertThat("response", response, hasContent(containsString("Crashed!")));
    }

    @Test public void
    notifiesFailureReporterWhenErrorOccurs() throws IOException {
        failsafe.reportFailuresTo(failureReporter);
        expectFailureReport();
        send(request);
    }

    private void expectFailureReport() {
        context.checking(new Expectations() {{
            oneOf(failureReporter).requestFailed(with(same(error)));
        }});
    }

    private void send(WebRequestBuilder request) throws IOException {
        response = client.loadWebResponse(request.build());
    }

    private Handler crashesWith(final Exception error) {
        return new Handler() {
            public void handle(Request request, Response response) throws Exception {
                throw error;
            }
        };
    }

    WebClient client = new WebClient();
    WebResponse response;
}