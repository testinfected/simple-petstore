package test.support.org.testinfected.petstore;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StackTrace {

    public static String of(Throwable t) {
        StringWriter stackTrace = new StringWriter();
        t.printStackTrace(new PrintWriter(stackTrace));
        return stackTrace.toString();
    }
}
