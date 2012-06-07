package test.org.testinfected.petstore;

import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.MimeType;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class MimeTypeTest {

    Map<String, String> mimeTypes = new HashMap<String, String>();

    @Before public void
    dataSet() {
        mimeTypes.put("a.txt", "text/plain");
        mimeTypes.put("b.txt", "text/plain");
        mimeTypes.put("logo.png", "image/png");
        mimeTypes.put("application.css", "text/css");
        mimeTypes.put("fav.ico", "image/x-icon");
        mimeTypes.put("main.html", "text/html");
    }

    @Test public void
    guessesMimeTypeFromExtension() {
        for (String url : mimeTypes.keySet()) {
            assertThat("media type of " + url, MimeType.guessFrom(url), equalTo(mimeTypes.get(url)));
        }
    }

    @Test public void
    assumesPlainTextWhenNotKnown() {
        assertThat("default media type", MimeType.guessFrom("unknown"), equalTo("text/plain"));
    }
}
