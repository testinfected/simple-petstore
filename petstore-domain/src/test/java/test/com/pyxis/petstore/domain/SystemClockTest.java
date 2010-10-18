package test.com.pyxis.petstore.domain;

import com.pyxis.petstore.domain.time.Clock;
import com.pyxis.petstore.domain.time.SystemClock;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SystemClockTest {
    Clock clock = new SystemClock();

    @Test public void
    timeIsAlwaysMidnight() {
        assertThat("seconds", clock.today(), withSeconds(equalTo(0)));
    }

    private FeatureMatcher<Date, Integer> withSeconds(Matcher<? super Integer> secondsMatcher) {
        return new FeatureMatcher<Date, Integer>(secondsMatcher, "a date with seconds", "seconds") {
            @Override
            protected Integer featureValueOf(Date actual) {
                return actual.getSeconds();
            }
        };
    }
}
