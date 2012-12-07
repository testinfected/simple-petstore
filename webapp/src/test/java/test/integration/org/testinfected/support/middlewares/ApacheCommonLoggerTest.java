package test.integration.org.testinfected.support.middlewares;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.support.Application;
import org.testinfected.support.MiddlewareStack;
import org.testinfected.support.Server;
import org.testinfected.support.simple.SimpleServer;
import org.testinfected.support.middlewares.ApacheCommonLogger;
import org.testinfected.time.Clock;
import test.support.org.testinfected.support.web.HttpRequest;

import java.util.Date;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.containsString;
import static org.testinfected.time.lib.DateBuilder.aDate;
import static test.support.org.testinfected.support.web.HttpRequest.aRequest;
import static test.support.org.testinfected.support.web.StaticResponse.respondWith;
import static test.support.org.testinfected.support.web.StaticResponse.respondWithCode;

// todo Consider rewriting as unit test now that we can mock requests and responses
@RunWith(JMock.class)
public class ApacheCommonLoggerTest {

    Mockery context = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    Logger logger = context.mock(Logger.class);
    Clock clock = context.mock(Clock.class);

    ApacheCommonLogger apacheCommonLogger = new ApacheCommonLogger(logger, clock);

    Server server = new SimpleServer(9999);
    HttpRequest request = aRequest().to(server);

    Date currentTime = aDate().onCalendar(2012, 6, 27).atTime(18, 4, 0).inZone("EDT").build();

    @Before public void
    stopClock() {
        context.checking(new Expectations() {{
            allowing(clock).now(); will(returnValue(currentTime));
        }});
    }

    @After public void
    stopServer() throws Exception {
        server.shutdown();
    }

    @Test public void
    logsServedRequestsUsingApacheCommonLogFormat() throws Exception {
        final String responseBody = "a response with a size of 28";
        Application app = new MiddlewareStack() {{
            use(apacheCommonLogger);
            run(respondWith(200, responseBody));
        }};
        server.run(app);

        context.checking(new Expectations() {{
            oneOf(logger).info(with("127.0.0.1 - - [27/Jun/2012:14:04:00 -0400] \"GET /products?keyword=dogs HTTP/1.1\" 200 28"));
        }});
        request.get("/products?keyword=dogs");
        Thread.sleep(50);
    }

    @Test public void
    hyphenReplacesContentSizeForEmptyResponses() throws Exception {
        Application app = new MiddlewareStack() {{
            use(apacheCommonLogger);
            run(respondWithCode(303));
        }};
        server.run(app);

        context.checking(new Expectations() {{
            oneOf(logger).info(with(containsString("\"DELETE /logout HTTP/1.1\" 303 -")));
        }});

        request.delete("/logout");
        Thread.sleep(50);
    }
}
