package com.vtence.molecule.util;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class MimeTypesTest {

    Map<String, String> knownTypes = new HashMap<String, String>();

    @Before public void
    dataSet() {
        knownTypes.put("a.txt", "text/plain");
        knownTypes.put("b.txt", "text/plain");
        knownTypes.put("logo.png", "image/png");
        knownTypes.put("application.css", "text/css");
        knownTypes.put("fav.ico", "image/x-icon");
        knownTypes.put("main.html", "text/html");
    }

    @Test public void
    guessesMimeTypeFromExtension() {
        for (String url : knownTypes.keySet()) {
            assertThat("media type of " + url, MimeTypes.guessFrom(url), equalTo(knownTypes.get(url)));
        }
    }

    @Test public void
    assumesPlainTextWhenNotKnown() {
        assertThat("default media type", MimeTypes.guessFrom("unknown"), equalTo("application/octet-stream"));
    }
}
