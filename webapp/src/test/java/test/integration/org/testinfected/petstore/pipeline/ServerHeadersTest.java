package test.integration.org.testinfected.petstore.pipeline;

import com.gargoylesoftware.htmlunit.WebResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.http.Status;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.Server;
import org.testinfected.petstore.pipeline.MiddlewareStack;
import org.testinfected.petstore.pipeline.ServerHeaders;
import org.testinfected.time.lib.BrokenClock;
import test.support.org.testinfected.petstore.web.WebRequestBuilder;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.time.lib.DateBuilder.aDate;
import static test.support.org.testinfected.petstore.web.EmptyResponse.respondWith;
import static test.support.org.testinfected.petstore.web.HasHeaderWithValue.hasHeader;
import static test.support.org.testinfected.petstore.web.HasStatusCode.hasStatusCode;
import static test.support.org.testinfected.petstore.web.WebRequestBuilder.aRequest;

public class ServerHeadersTest {

    Application application = new MiddlewareStack() {{
        use(new ServerHeaders(BrokenClock.stoppedAt(aDate().onCalendar(2012, 6, 8).atMidnight().build())));
        run(respondWith(Status.OK));
    }};

    int SERVER_LISTENING_PORT = 9999;
    Server server = new Server(SERVER_LISTENING_PORT);
    WebRequestBuilder request = aRequest().onPort(SERVER_LISTENING_PORT);

    @Before public void
    startServer() throws IOException {
        server.run(application);
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    setsResponseHeaders() throws IOException {
        WebResponse response = request.send();

        assertThat("response", response, hasHeader("Server", Server.NAME));
        assertThat("response", response, hasHeader("Date", "Fri, 08 Jun 2012 04:00:00 GMT"));
    }

    @Test public void
    forwardsToNextApplication() throws IOException {
        WebResponse response = request.send();

        assertThat("response", response, hasStatusCode(Status.OK.getCode()));
    }
}