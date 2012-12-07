package test.unit.org.testinfected.support.util;

import org.junit.Test;
import org.testinfected.support.util.ConsoleErrorReporter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConsoleErrorReporterTest {

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    ConsoleErrorReporter consoleReporter = new ConsoleErrorReporter(new PrintStream(output));

    @Test public void
    writesInternalErrorsToOutput() {
        Exception failure = new Exception("Bad!");
        failure.fillInStackTrace();

        consoleReporter.errorOccurred(failure);
        assertThat("output", output.toString(), containsString("[ERROR] Internal error"));
        assertThat("output", output.toString(), containsString(stackTraceOf(failure)));
    }

    private String stackTraceOf(Exception exception) {
        StringWriter capture = new StringWriter();
        exception.printStackTrace(new PrintWriter(capture));
        capture.flush();
        return capture.toString();
    }
}
