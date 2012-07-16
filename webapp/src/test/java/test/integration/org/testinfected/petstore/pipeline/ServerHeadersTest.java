package test.integration.org.testinfected.petstore.pipeline;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.Application;
import org.testinfected.petstore.Server;
import org.testinfected.petstore.pipeline.MiddlewareStack;
import org.testinfected.petstore.pipeline.ServerHeaders;
import org.testinfected.time.lib.BrokenClock;
import test.support.org.testinfected.petstore.web.HttpRequest;
import test.support.org.testinfected.petstore.web.HttpResponse;

import java.io.IOException;

import static org.simpleframework.http.Status.SEE_OTHER;
import static org.testinfected.time.lib.DateBuilder.aDate;
import static test.support.org.testinfected.petstore.web.HttpRequest.aRequest;
import static test.support.org.testinfected.petstore.web.StaticResponse.respondWith;

public class ServerHeadersTest {

    Application application = new MiddlewareStack() {{
        use(new ServerHeaders(BrokenClock.stoppedAt(aDate().onCalendar(2012, 6, 8).atMidnight().build())));
        run(respondWith(SEE_OTHER));
    }};

    Server server = new Server(9999);
    HttpRequest request = aRequest().to(server);

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
        HttpResponse response = request.get("/");

        response.assertHasHeader("Server", Server.NAME);
        response.assertHasHeader("Date", "Fri, 08 Jun 2012 04:00:00 GMT");
    }

    @Test public void
    forwardsToNextApplication() throws IOException {
        request.get("/").assertHasStatusCode(SEE_OTHER.getCode());
    }
}