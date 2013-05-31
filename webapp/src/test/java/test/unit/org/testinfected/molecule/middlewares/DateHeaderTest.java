package test.unit.org.testinfected.molecule.middlewares;

import org.junit.Test;
import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.middlewares.DateHeader;
import test.support.org.testinfected.molecule.unit.BrokenClock;
import test.support.org.testinfected.molecule.unit.MockRequest;
import test.support.org.testinfected.molecule.unit.MockResponse;

import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.org.testinfected.molecule.unit.DateBuilder.calendarDate;
import static test.support.org.testinfected.molecule.unit.MockRequest.aRequest;
import static test.support.org.testinfected.molecule.unit.MockResponse.aResponse;

public class DateHeaderTest {
    Date now = calendarDate(2012, 6, 8).atMidnight().build();
    DateHeader dateHeader = new DateHeader(BrokenClock.stoppedAt(now));

    MockRequest request = aRequest();
    MockResponse response = aResponse();

    @Test public void
    setsDateHeaderFromClockTime() throws Exception {
        dateHeader.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body(response.header("Date"));
            }
        });
        dateHeader.handle(request, response);
        assertDate("Fri, 08 Jun 2012 04:00:00 GMT");
    }

    private void assertDate(String date) {
        assertThat("date header", response.body(), equalTo(date));
    }
}