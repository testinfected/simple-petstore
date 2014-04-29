package com.vtence.molecule.lib;

import com.vtence.molecule.FailureReporter;

import java.io.PrintStream;

public class PlainErrorReporter implements FailureReporter {

    private final PrintStream out;

    public static FailureReporter toStandardError() {
        return new PlainErrorReporter(System.err);
    }

    public PlainErrorReporter(PrintStream out) {
        this.out = out;
    }

    public void errorOccurred(Throwable error) {
        error("Internal error", error);
    }

    private void error(final String msg, Throwable failure) {
        out.println("[ERROR] " + msg);
        failure.printStackTrace(out);
    }
}

