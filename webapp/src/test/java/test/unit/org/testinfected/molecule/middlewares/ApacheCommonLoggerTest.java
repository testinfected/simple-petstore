package test.unit.org.testinfected.molecule.middlewares;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.HttpStatus;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.middlewares.ApacheCommonLogger;
import test.support.org.testinfected.molecule.unit.BrokenClock;

import java.util.Date;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.containsString;
import static org.testinfected.molecule.HttpMethod.DELETE;
import static org.testinfected.molecule.HttpMethod.GET;
import static test.support.org.testinfected.molecule.unit.DateBuilder.calendarDate;
import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

@RunWith(JMock.class)
public class ApacheCommonLoggerTest {

    Mockery context = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    Logger logger = context.mock(Logger.class);
    Date now = calendarDate(2012, 6, 27).atTime(18, 4, 0).inZone("EDT").build();
    ApacheCommonLogger apacheCommonLogger = new ApacheCommonLogger(logger, BrokenClock.stoppedAt(now));

    @Test public void
    logsRequestsServedInApacheCommonLogFormat() throws Exception {
        Request request = aRequest().withIp("192.168.0.1").withMethod(GET).withPath("/products?keyword=dogs");
        apacheCommonLogger.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body("a response with a size of 28");
                response.contentLength(28);
                response.status(HttpStatus.OK);
            }
        });

        context.checking(new Expectations() {{
            oneOf(logger).info("192.168.0.1 - - [27/Jun/2012:14:04:00 -0400] \"GET /products?keyword=dogs HTTP/1.1\" 200 28");
        }});

        apacheCommonLogger.handle(request, aResponse());
    }

    @Test
    public void
    hyphenReplacesContentSizeForEmptyResponses() throws Exception {
        Request request = aRequest().withIp("192.168.0.1").withMethod(DELETE).withPath("/logout");
        apacheCommonLogger.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body("");
                response.contentLength(0);
                response.status(HttpStatus.NO_CONTENT);
            }
        });

        context.checking(new Expectations() {{
            oneOf(logger).info(with(containsString("\"DELETE /logout HTTP/1.1\" 204 -")));
        }});

        apacheCommonLogger.handle(request, aResponse());
    }
}
