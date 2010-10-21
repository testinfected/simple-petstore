package test.com.pyxis.petstore.nist;

import com.pyxis.petstore.nist.NISTDialect;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static test.support.com.pyxis.petstore.builders.DateBuilder.aDate;

public class NISTDialectTest {

    NISTDialect dialect = new NISTDialect();
    TimeZone utc = TimeZone.getTimeZone("UTC");
    String serverOutput = "55488 10-10-19 16:03:15 20 0 0 448.0 UTC(NIST) *";

    @Test public void
    translatesOutputToPointInUTCTime() throws ParseException {
        Date serverTime = aDate().onCalendar(2010, 10, 19).atTime(16, 3, 15).in(utc).build();
        assertThat("server time", dialect.translate(serverOutput), equalTo(serverTime));
    }
}
