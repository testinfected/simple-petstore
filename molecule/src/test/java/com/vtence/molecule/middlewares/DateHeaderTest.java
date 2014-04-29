package com.vtence.molecule.middlewares;

import com.vtence.molecule.support.BrokenClock;
import com.vtence.molecule.support.MockRequest;
import com.vtence.molecule.support.MockResponse;
import org.junit.Test;
import com.vtence.molecule.Application;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;

import java.util.Date;

import static com.vtence.molecule.support.DateBuilder.calendarDate;
import static com.vtence.molecule.support.MockRequest.aRequest;
import static com.vtence.molecule.support.MockResponse.aResponse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DateHeaderTest {
    Date now = calendarDate(2012, 6, 8).atMidnight().inZone("GMT-04:00").build();
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