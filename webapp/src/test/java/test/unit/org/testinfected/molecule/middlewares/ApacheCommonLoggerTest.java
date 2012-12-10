package test.unit.org.testinfected.molecule.middlewares;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.middlewares.ApacheCommonLogger;
import org.testinfected.time.Clock;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import java.util.Date;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.containsString;
import static org.testinfected.molecule.HttpMethod.DELETE;
import static org.testinfected.molecule.HttpMethod.GET;
import static org.testinfected.molecule.HttpStatus.NO_CONTENT;
import static org.testinfected.molecule.HttpStatus.OK;
import static org.testinfected.time.lib.DateBuilder.aDate;
import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;
import static test.support.org.testinfected.molecule.unit.SetHeader.setHeader;
import static test.support.org.testinfected.molecule.unit.SetStatus.setStatus;
import static test.support.org.testinfected.molecule.unit.WriteBody.writeBody;

@RunWith(JMock.class)
public class ApacheCommonLoggerTest {

    Mockery context = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    Logger logger = context.mock(Logger.class);
    Clock clock = context.mock(Clock.class);
    Application successor = context.mock(Application.class, "successor");
    ApacheCommonLogger apacheCommonLogger = new ApacheCommonLogger(logger, clock);

    Date currentTime = aDate().onCalendar(2012, 6, 27).atTime(18, 4, 0).inZone("EDT").build();

    MockRequest request = aRequest().withIp("192.168.0.1");
    MockResponse response = aResponse();

    @Before public void
    stopClock() {
        context.checking(new Expectations() {{
            allowing(clock).now();
            will(returnValue(currentTime));
        }});
    }

    @Before public void
    chainWithSuccessor()  {
        apacheCommonLogger.connectTo(successor);
    }

    @Test public void
    logsRequestsServedInApacheCommonLogFormat() throws Exception {
        final String responseBody = "a response with a size of 28";
        context.checking(new Expectations() {{
            allowing(successor).handle(with(request), with(response)); will(doAll(writeBody(responseBody), setStatus(OK), setHeader("Content-Length", 28)));
            oneOf(logger).info(with("192.168.0.1 - - [27/Jun/2012:14:04:00 -0400] \"GET /products?keyword=dogs HTTP/1.1\" 200 28"));
        }});

        request.withMethod(GET).withPath("/products?keyword=dogs");
        apacheCommonLogger.handle(request, response);
    }

    @Test
    public void
    hyphenReplacesContentSizeForEmptyResponses() throws Exception {
        context.checking(new Expectations() {{
            allowing(successor).handle(with(request), with(response)); will(doAll(writeBody(""), setStatus(NO_CONTENT)));
            oneOf(logger).info(with(containsString("\"DELETE /logout HTTP/1.1\" 204 -")));
        }});

        request.withMethod(DELETE).withPath("/logout");
        apacheCommonLogger.handle(request, response);
    }
}
