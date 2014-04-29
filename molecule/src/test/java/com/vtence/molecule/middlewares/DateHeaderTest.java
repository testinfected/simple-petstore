package com.vtence.molecule.middlewares;

import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.support.BrokenClock;
import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.junit.Test;

import java.util.Date;

import static com.vtence.molecule.support.Dates.calendarDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DateHeaderTest {
    Date now = calendarDate(2012, 6, 8).atMidnight().inZone("GMT-04:00").toDate();
    DateHeader dateHeader = new DateHeader(BrokenClock.stoppedAt(now));

    MockRequest request = new MockRequest();
    MockResponse response = new MockResponse();

    @Test public void
    setsDateHeaderFromClockTime() throws Exception {
        dateHeader.connectTo(new Application() {
            public void handle(Request request, Response response) throws Exception {
                response.body(response.get("Date"));
            }
        });
        dateHeader.handle(request, response);
        assertDate("Fri, 08 Jun 2012 04:00:00 GMT");
    }

    private void assertDate(String date) {
        assertThat("date header", response.text(), equalTo(date));
    }
}