package test.unit.org.testinfected.petstore.util;

import org.junit.Test;
import org.testinfected.petstore.lib.BundledMessages;

import java.time.Instant;
import java.util.Date;
import java.util.ListResourceBundle;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

public class BundledMessagesTest {

    BundledMessages messages = new BundledMessages(new ListResourceBundle() {
        protected Object[][] getContents() {
            return new Object[][]{
                    {"blank", "may not be blank"},
                    {"out-of-range", "must be between {0, number} and {1, number}"},
                    {"expired", "must be after {0, date}"}
            };
        }
        public Locale getLocale() { return Locale.CANADA_FRENCH; }
    });

    @Test public void
    readsMessagesFromResourceBundle() {
        assertThat("formatted message", messages.interpolate("blank"), equalTo("may not be blank"));
    }

    @Test public void
    usesProvidedParametersAsMessageArguments() {
        assertThat("formatted message", messages.interpolate("out-of-range", 1, 10), equalTo("must be between 1 and 10"));
    }

    @Test public void
    usesBundleLocaleWhenFormattingParameters() {
        assertThat("formatted message", messages.interpolate("expired", Date.from(Instant.parse("2013-07-05T00:00:00.00Z"))),
                startsWith("must be after 2013-07"));
    }
}
