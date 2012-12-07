package org.testinfected.support.util;

import java.io.PrintStream;

public class ConsoleErrorReporter implements FailureReporter {

    private final PrintStream console;

    public static FailureReporter toStandardError() {
        return new ConsoleErrorReporter(System.err);
    }

    public ConsoleErrorReporter(PrintStream console) {
        this.console = console;
    }

    public void errorOccurred(Exception error) {
        error("Internal error", error);
    }

    private void error(final String msg, Exception failure) {
        console.println("[ERROR] " + msg);
        failure.printStackTrace(console);
    }
}

