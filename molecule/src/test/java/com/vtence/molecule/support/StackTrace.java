package com.vtence.molecule.support;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class StackTrace {

    private StackTrace() {}

    public static String of(Throwable t) {
        StringWriter stackTrace = new StringWriter();
        t.printStackTrace(new PrintWriter(stackTrace));
        return stackTrace.toString();
    }
}
