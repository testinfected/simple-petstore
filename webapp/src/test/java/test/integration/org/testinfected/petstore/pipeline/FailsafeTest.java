package test.integration.org.testinfected.petstore.pipeline;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.testinfected.petstore.Handler;
import org.testinfected.petstore.Server;
import org.testinfected.petstore.pipeline.Application;
import org.testinfected.petstore.pipeline.Failsafe;
import test.support.org.testinfected.petstore.web.OfflineContext;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.org.testinfected.petstore.web.HasHeaderWithValue.hasHeader;
import static test.support.org.testinfected.petstore.web.HasStatusCode.hasStatusCode;
import static test.support.org.testinfected.petstore.web.OfflineContext.offline;
import static test.support.org.testinfected.petstore.web.WebRequestBuilder.aRequest;

public class FailsafeTest {

    Application application = new Application() {{
        use(new Failsafe(offline().renderer()));
        run(crashesWith(new Exception("Crashed!")));
    }};

    int PORT = 9999;
    Server server = new Server(PORT);
    WebClient client = new WebClient();
    WebResponse response;

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
        response = client.loadWebResponse(aRequest().onPort(PORT).build());
        assertThat("response", response, hasStatusCode(500));
        assertThat("response", response, hasHeader("Content-Type", containsString("text/html")));
        assertThat("content", response.getContentAsString(), containsString("Crashed!"));
    }

    private Handler crashesWith(final Exception error) {
        return new Handler() {
            public void handle(Request request, Response response) throws Exception {
                throw error;
            }
        };
    }
}