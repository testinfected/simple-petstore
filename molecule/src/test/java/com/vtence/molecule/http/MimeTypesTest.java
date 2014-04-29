package com.vtence.molecule.http;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class MimeTypesTest {

    Map<String, String> knownTypes = new HashMap<String, String>();

    @Before public void
    defaultKnownTypes() {
        knownTypes.put("main.html", "text/html");
        knownTypes.put("plain.txt", "text/plain");
        knownTypes.put("application.css", "text/css");
        knownTypes.put("application.js", "application/javascript");
        knownTypes.put("logo.png", "image/png");
        knownTypes.put("fav.ico", "image/x-icon");
        knownTypes.put("image.gif", "image/gif");
        knownTypes.put("image.jpg", "image/jpeg");
        knownTypes.put("image.jpeg", "image/jpeg");
    }

    @Test public void
    guessesTypesFromFileExtension() {
        for (String filename : knownTypes.keySet()) {
            assertThat("media type of " + filename, MimeTypes.defaults().guessFrom(filename),
                    equalTo(knownTypes.get(filename)));
        }
    }

    @Test public void
    letsRedefineKnownTypes() {
        MimeTypes types = MimeTypes.defaults();
        types.register("css", "text/new-css");
        assertThat("media type", types.guessFrom("style.css"), equalTo("text/new-css"));
    }

    @Test public void
    canLearnNewTypes() {
        MimeTypes types = MimeTypes.defaults();
        types.register("bar", "application/bar");
        assertThat("media type", types.guessFrom("file.bar"), equalTo("application/bar"));
    }

    @Test public void
    assumesPlainTextWhenNotKnown() {
        assertThat("default media type", new MimeTypes().guessFrom("unknown"),
                equalTo("application/octet-stream"));
    }
}
