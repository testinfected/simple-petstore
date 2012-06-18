package org.testinfected.petstore.util;

import org.testinfected.petstore.FailureReporter;

import java.io.IOException;
import java.io.PrintStream;

public class ConsoleErrorReporter implements FailureReporter {

    private final PrintStream console;

    public ConsoleErrorReporter() {
        this(System.err);
    }

    public ConsoleErrorReporter(PrintStream console) {
        this.console = console;
    }

    public void internalErrorOccurred(Exception failure) {
        error("Internal error");
        failure.printStackTrace(console);
    }

    public void communicationFailed(IOException failure) {
        error("Communication failure");
        failure.printStackTrace(console);
    }

    private void error(final String msg) {
        console.println("[ERROR] " + msg);
    }
}

