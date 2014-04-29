package com.vtence.molecule.lib;

import com.vtence.molecule.support.StackTrace;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class PlainErrorReporterTest {

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PlainErrorReporter consoleReporter = new PlainErrorReporter(new PrintStream(output));

    @Test public void
    writesInternalErrorsToOutput() {
        Exception failure = new Exception("Bad!");
        failure.fillInStackTrace();

        consoleReporter.errorOccurred(failure);
        assertThat("output", output.toString(), containsString("[ERROR] Internal error"));
        assertThat("output", output.toString(), containsString(StackTrace.of(failure)));
    }
}
