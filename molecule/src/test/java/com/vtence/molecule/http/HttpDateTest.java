package com.vtence.molecule.http;

import org.junit.Test;

import java.util.Date;

import static com.vtence.molecule.support.Dates.calendarDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class HttpDateTest {

    Date date = calendarDate(2014, 3, 6).atTime(8, 49, 37).toDate();

    @Test public void
    parsesRfc1123Dates() {
        assertThat("date", HttpDate.parse("Thu, 06 Mar 2014 08:49:37 GMT"), equalTo(date));
    }

    @Test public void
    parsesRfc1036Dates() {
        assertThat("date", HttpDate.parse("Thu, 06-Mar-14 08:49:37 GMT"), equalTo(date));
    }

    @Test public void
    parsesAscTimeDates() {
        assertThat("date", HttpDate.parse("Thu Mar  6 08:49:37 2014"), equalTo(date));
    }

    @Test public void
    formatsDatesAccordingToRfc1123() {
        assertThat("http date", HttpDate.format(date), equalTo("Thu, 06 Mar 2014 08:49:37 GMT"));
    }
}
