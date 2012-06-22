package org.testinfected.petstore.util;

import org.testinfected.petstore.FailureReporter;

import java.io.IOException;
import java.io.PrintStream;

public class ConsoleErrorReporter implements FailureReporter {

    private final PrintStream console;

    public static FailureReporter toStandardError() {
        return new ConsoleErrorReporter(System.err);
    }

    public ConsoleErrorReporter(PrintStream console) {
        this.console = console;
    }

    public void internalErrorOccurred(Exception failure) {
        error("Internal error", failure);
    }

    public void communicationFailed(IOException failure) {
        error("Communication failure", failure);
    }

    private void error(final String msg, Exception failure) {
        console.println("[ERROR] " + msg);
        failure.printStackTrace(console);
    }
}

