package test.unit.org.testinfected.molecule.util;

import org.junit.Before;
import org.junit.Test;
import org.testinfected.molecule.util.MimeTypes;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class MimeTypeTest {

    Map<String, String> expectations = new HashMap<String, String>();

    @Before public void
    dataSet() {
        expectations.put("a.txt", "text/plain");
        expectations.put("b.txt", "text/plain");
        expectations.put("logo.png", "image/png");
        expectations.put("application.css", "text/css");
        expectations.put("fav.ico", "image/x-icon");
        expectations.put("main.html", "text/html");
    }

    @Test public void
    guessesMimeTypeFromExtension() {
        for (String url : expectations.keySet()) {
            assertThat("media type of " + url, MimeTypes.guessFrom(url), equalTo(expectations.get(url)));
        }
    }

    @Test public void
    assumesPlainTextWhenNotKnown() {
        assertThat("default media type", MimeTypes.guessFrom("unknown"), equalTo("application/octet-stream"));
    }
}
