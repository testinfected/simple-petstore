package com.vtence.molecule.session;

import com.vtence.molecule.support.BrokenClock;
import org.hamcrest.Matcher;
import org.junit.Test;
import com.vtence.molecule.Session;
import com.vtence.molecule.util.Clock;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.vtence.molecule.support.DateBuilder.namedDate;
import static java.util.concurrent.TimeUnit.DAYS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.equalTo;

public class SessionHashTest {

    long TIMEOUT = TimeUnit.MINUTES.toSeconds(30);
    Date creationTime = namedDate("creation time").build();
    Session session = new SessionHash("session-id", creationTime);

    @Test public void
    isInitiallyValid() {
        assertThat("id", session.id(), equalTo("session-id"));
        assertThat("invalid", session.invalid(), equalTo(false));
    }

    @Test public void
    isInitiallyEmpty() {
        assertThat("attribute keys", session.keys(), emptyIterable());
        assertThat("attribute values", session.values(), emptyIterable());
    }

    @Test public void
    storesAndRestoresAttributes() {
        session.put("A1", "V1");
        assertThat("A1?", session.contains("A1"), equalTo(true));
        assertThat("A1", session.<String>get("A1"), equalTo("V1"));
    }

    @Test public void
    storesMultipleAttributes() {
        session.put("A1", "V1");
        session.put("A2", "V2");
        session.put("A3", "V3");
        assertThat("A1", session.<String>get("A1"), equalTo("V1"));
        assertThat("A2", session.<String>get("A2"), equalTo("V2"));
        assertThat("A3", session.<String>get("A3"), equalTo("V3"));
    }

    @Test public void
    allowsOverridingAttributes() {
        session.put("A1", "V1");
        session.put("A1", "V1.1");
        assertThat("A1", session.<String>get("A1"), equalTo("V1.1"));
    }

    @Test public void
    knowsAttributesKeysAndValues() {
        session.put("A1", "1");
        session.put("A2", "2");
        session.put("A3", "3");
        session.put("A4", "4");
        session.put("A5", "5");

        assertThat("attribute keys", session.keys(), containsItems("A1", "A2", "A3", "A4", "A5"));
        assertThat("attribute values", session.values(), containsItems("1", "2", "3", "4", "5"));
    }

    @Test public void
    isInitiallySetToNeverExpire() {
        assertThat("timeout", session.timeout(), equalTo(0L));
        assertThat("expired after 1 day", session.expired(clockSet(aDayAfter(creationTime))), equalTo(false));
        assertThat("expired after 1 year", session.expired(clockSet(aYearAfter(creationTime))), equalTo(false));
    }

    @Test public void
    expiresAfterTimeout() {
        session.timeout(TIMEOUT);

        assertThat("expired before timeout", session.expired(clockSet(justBefore(whenTimeoutOccurs(creationTime)))), equalTo(false));
        assertThat("expired at timeout", session.expired(clockSet(whenTimeoutOccurs(creationTime))), equalTo(true));
        assertThat("expired after timeout", session.expired(clockSet(justAfter(whenTimeoutOccurs(creationTime)))), equalTo(true));
    }

    @Test public void
    resetsTimeoutWhenTouched() {
        session.timeout(TIMEOUT);

        Date lastAccessTime = aDayAfter(creationTime);
        session.touch(clockSet(lastAccessTime));

        assertThat("expired after initial timeout", session.expired(clockSet(whenTimeoutOccurs(creationTime))), equalTo(false));
        assertThat("expired at reset timeout", session.expired(clockSet(whenTimeoutOccurs(lastAccessTime))), equalTo(true));
    }

    @Test public void
    informsOfCreationAndAccessTime() {
        Date lastAccessTime = namedDate("last access time").build();
        session.touch(clockSet(lastAccessTime));

        assertThat("creation time", session.createdAt(), equalTo(creationTime));
        assertThat("last access time", session.lastAccessedAt(), equalTo(lastAccessTime));
    }

    @Test public void
    dropsContentWhenInvalidated() {
        session.put("A1", "V1");
        session.put("A2", "V2");
        session.put("A3", "V3");

        session.invalidate();

        assertThat("invalid", session.invalid(), equalTo(true));
        assertThat("attribute keys", session.keys(), emptyIterable());
        assertThat("attribute values", session.values(), emptyIterable());
    }

    private Clock clockSet(Date pointInTime) {
        return BrokenClock.stoppedAt(pointInTime);
    }

    private Date justBefore(Date pointInTime) {
        return at(pointInTime, -1);
    }

    private Date justAfter(Date pointInTime) {
        return at(pointInTime, +1);
    }

    private Date whenTimeoutOccurs(Date pointInTime) {
        return at(pointInTime, TimeUnit.SECONDS.toMillis(TIMEOUT));
    }

    private Date aDayAfter(Date pointInTime) {
        return at(pointInTime, TimeUnit.DAYS.toMillis(1));
    }

    private Date aYearAfter(Date pointInTime) {
        return at(pointInTime, DAYS.toMillis(365));
    }

    private Date at(Date currentTime, long millis) {
        return new Date(currentTime.getTime() + millis);
    }

    private Matcher<Iterable<?>> containsItems(Object... items) {
        return containsInAnyOrder(items);
    }
}
