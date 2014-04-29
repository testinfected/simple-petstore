package com.vtence.molecule.middlewares;

import com.vtence.molecule.support.BrokenClock;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Rule;
import org.junit.Test;
import com.vtence.molecule.Application;
import com.vtence.molecule.HttpStatus;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;

import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

import static com.vtence.molecule.support.DateBuilder.calendarDate;
import static com.vtence.molecule.support.MockRequest.aRequest;
import static com.vtence.molecule.support.MockResponse.aResponse;
import static org.hamcrest.CoreMatchers.containsString;
import static com.vtence.molecule.HttpMethod.DELETE;
import static com.vtence.molecule.HttpMethod.GET;

public class ApacheCommonLoggerTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
        setThreadingPolicy(new Synchroniser());
    }};

    Logger logger = context.mock(Logger.class);
    Date now = calendarDate(2012, 6, 27).atTime(18, 4, 0).inZone("GMT").build();
    ApacheCommonLogger apacheCommonLogger = new ApacheCommonLogger(logger, BrokenClock.stoppedAt(now), TimeZone.getTimeZone("GMT+01:00"));

    @Test public void
    logsRequestsServedInApacheCommonLogFormat() throws Exception {
        Request request = aRequest().withIp("192.168.0.1").withMethod(GET).withPath("/products?keyword=dogs");
        apacheCommonLogger.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.contentLength(28);
                response.body("a response with a size of 28");
                response.status(HttpStatus.OK);
            }
        });

        context.checking(new Expectations() {{
            oneOf(logger).info("192.168.0.1 - - [27/Jun/2012:19:04:00 +0100] \"GET /products?keyword=dogs HTTP/1.1\" 200 28");
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
