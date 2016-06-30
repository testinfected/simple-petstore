package org.testinfected.petstore;

import com.vtence.molecule.FailureReporter;

import java.io.PrintStream;

public class PlainErrorReporter implements FailureReporter {
    private final PrintStream out;

    public PlainErrorReporter(PrintStream out) {
        this.out = out;
    }

    public static FailureReporter toStandardError() {
        return new PlainErrorReporter(System.err);
    }

    public void errorOccurred(Throwable error) {
        out.print("[ERROR] Internal Server Error");
        error.printStackTrace(out);
    }
}
