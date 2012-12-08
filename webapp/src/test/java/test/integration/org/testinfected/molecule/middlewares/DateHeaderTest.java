package test.integration.org.testinfected.molecule.middlewares;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.molecule.MiddlewareStack;
import org.testinfected.molecule.Server;
import org.testinfected.molecule.middlewares.DateHeader;
import org.testinfected.molecule.simple.SimpleServer;
import org.testinfected.time.lib.BrokenClock;
import test.support.org.testinfected.molecule.web.HttpRequest;
import test.support.org.testinfected.molecule.web.HttpResponse;

import java.io.IOException;
import java.util.Date;

import static org.testinfected.molecule.HttpStatus.SEE_OTHER;
import static org.testinfected.time.lib.DateBuilder.aDate;
import static test.support.org.testinfected.molecule.web.HttpRequest.aRequest;
import static test.support.org.testinfected.molecule.web.StaticResponse.respondWith;

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