package com.vtence.molecule.middlewares;

import com.vtence.molecule.Application;
import com.vtence.molecule.http.HttpStatus;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.support.BrokenClock;
import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

import static com.vtence.molecule.http.HttpMethod.DELETE;
import static com.vtence.molecule.http.HttpMethod.GET;
import static com.vtence.molecule.support.Dates.calendarDate;
import static org.hamcrest.CoreMatchers.containsString;

public class ApacheCommonLoggerTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
        setThreadingPolicy(new Synchroniser());
    }};
    Logger logger = context.mock(Logger.class);
    Date currentTime = calendarDate(2012, 6, 27).atTime(12, 4, 0).inZone("GMT-05:00").toDate();
    ApacheCommonLogger apacheCommonLogger =
            new ApacheCommonLogger(logger, BrokenClock.stoppedAt(currentTime), TimeZone.getTimeZone("GMT+01:00"));

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    @Test public void
    logsRequestsServedInApacheCommonLogFormat() throws Exception {
        request.remoteIp("192.168.0.1").method(GET).uri("/products?keyword=dogs");
        apacheCommonLogger.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body("a response with a size of 28");
                response.status(HttpStatus.OK);
            }
        });
        context.checking(new Expectations() {{
            oneOf(logger).info("192.168.0.1 - - [27/Jun/2012:18:04:00 +0100] \"GET /products?keyword=dogs HTTP/1.1\" 200 28");
        }});

        apacheCommonLogger.handle(request, response);
    }

    @Test public void
    hyphenReplacesContentSizeForEmptyResponses() throws Exception {
        request.remoteIp("192.168.0.1").method(DELETE).uri("/logout");
        apacheCommonLogger.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body("");
                response.status(HttpStatus.NO_CONTENT);
            }
        });

        context.checking(new Expectations() {{
            oneOf(logger).info(with(containsString("\"DELETE /logout HTTP/1.1\" 204 -")));
        }});

        apacheCommonLogger.handle(request, response);
    }
}
