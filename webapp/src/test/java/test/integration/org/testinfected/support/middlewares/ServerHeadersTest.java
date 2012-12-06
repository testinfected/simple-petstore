package test.integration.org.testinfected.support.middlewares;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.support.HttpStatus;
import org.testinfected.support.MiddlewareStack;
import org.testinfected.support.Server;
import org.testinfected.support.middlewares.ServerHeaders;
import org.testinfected.time.lib.BrokenClock;
import test.support.org.testinfected.support.web.HttpRequest;
import test.support.org.testinfected.support.web.HttpResponse;

import java.io.IOException;

import static org.simpleframework.http.Status.SEE_OTHER;
import static org.testinfected.time.lib.DateBuilder.aDate;
import static test.support.org.testinfected.support.web.HttpRequest.aRequest;
import static test.support.org.testinfected.support.web.StaticResponse.respondWith;

public class ServerHeadersTest {

    ServerHeaders serverHeaders = new ServerHeaders(BrokenClock.stoppedAt(aDate().onCalendar(2012, 6, 8).atMidnight().build()));

    Server server = new Server(9999);
    HttpRequest request = aRequest().to(server);

    @Before public void
    startServer() throws IOException {
        server.run(new MiddlewareStack() {{
            use(serverHeaders);
            run(respondWith(HttpStatus.SEE_OTHER));
        }});
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    setsResponseHeaders() throws IOException {
        HttpResponse response = request.get("/");

        response.assertHasHeader("Server", Server.NAME);
        response.assertHasHeader("Date", "Fri, 08 Jun 2012 04:00:00 GMT");
    }

    @Test public void
    forwardsToNextApplication() throws IOException {
        request.send().assertHasStatusCode(SEE_OTHER.getCode());
    }
}