package test.integration.org.testinfected.support.middlewares;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.support.MiddlewareStack;
import org.testinfected.support.Server;
import org.testinfected.support.middlewares.DateHeader;
import org.testinfected.support.simple.SimpleServer;
import org.testinfected.time.lib.BrokenClock;
import test.support.org.testinfected.support.web.HttpRequest;
import test.support.org.testinfected.support.web.HttpResponse;

import java.io.IOException;
import java.util.Date;

import static org.testinfected.support.HttpStatus.SEE_OTHER;
import static org.testinfected.time.lib.DateBuilder.aDate;
import static test.support.org.testinfected.support.web.HttpRequest.aRequest;
import static test.support.org.testinfected.support.web.StaticResponse.respondWith;

// todo Consider rewriting as unit test now that we can mock requests and responses
public class DateHeaderTest {

    Date now = aDate().onCalendar(2012, 6, 8).atMidnight().build();
    DateHeader dateHeader = new DateHeader(BrokenClock.stoppedAt(now));

    Server server = new SimpleServer(9999);
    HttpRequest request = aRequest().to(server);

    @Before public void
    startServer() throws IOException {
        server.run(new MiddlewareStack() {{
            use(dateHeader);
            run(respondWith(SEE_OTHER));
        }});
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    setsDateHeaderFromClockTime() throws IOException {
        HttpResponse response = request.get("/");
        response.assertHasHeader("Date", "Fri, 08 Jun 2012 04:00:00 GMT");
    }

    @Test public void
    forwardsToNextApplication() throws IOException {
        request.send().assertHasStatusCode(SEE_OTHER.code);
    }
}