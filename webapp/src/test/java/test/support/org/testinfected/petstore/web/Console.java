package test.support.org.testinfected.petstore.web;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class Console {
    
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final PrintStream standardOut;
    
    public static Console captureStandardOutput() {
        return new Console();
    }

    private Console() {
        standardOut = System.out;
        System.setOut(new PrintStream(buffer));
    }
    
    public void terminate() {
        System.setOut(standardOut);
    }

    public void assertHasEntry(Matcher<? super String> matcher) {
        MatcherAssert.assertThat(buffer.toString(), matcher);
    }
}