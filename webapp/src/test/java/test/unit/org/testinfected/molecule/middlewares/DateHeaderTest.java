package test.unit.org.testinfected.molecule.middlewares;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.middlewares.DateHeader;
import org.testinfected.time.lib.BrokenClock;
import test.support.org.testinfected.molecule.web.MockRequest;
import test.support.org.testinfected.molecule.web.MockResponse;

import java.util.Date;

import static org.testinfected.time.lib.DateBuilder.aDate;
import static test.support.org.testinfected.molecule.web.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.web.MockResponse.aResponse;

@RunWith(JMock.class)
public class DateHeaderTest {
    Mockery context = new JUnit4Mockery();

    Date now = aDate().onCalendar(2012, 6, 8).atMidnight().build();
    DateHeader dateHeader = new DateHeader(BrokenClock.stoppedAt(now));
    Application successor = context.mock(Application.class, "successor");

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Before public void
    chainWithSuccessor()  {
        dateHeader.connectTo(successor);
    }

    @Test public void
    setsDateHeaderFromClockTime() throws Exception {
        context.checking(new Expectations() {{
            oneOf(successor).handle(with(request), with(response));
        }});

        dateHeader.handle(request, response);
        response.assertHeader("Date", "Fri, 08 Jun 2012 04:00:00 GMT");
    }
}