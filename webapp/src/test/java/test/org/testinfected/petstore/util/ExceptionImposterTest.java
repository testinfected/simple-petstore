package test.org.testinfected.petstore.util;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.testinfected.petstore.util.ExceptionImposter;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testinfected.petstore.util.ExceptionImposter.imposterize;

public class ExceptionImposterTest {
    Exception original;

    @Test public void
    leavesUncheckedExceptionsUnchanged() {
        original = new RuntimeException();
        assertThat("instance", imposterize(original), sameInstance(original));
    }

    @Test public void
    imposterizesCheckedExceptions() {
        original = new Exception();
        assertThat("type", imposterize(original), instanceOf(ExceptionImposter.class));
        assertThat("cause", ((ExceptionImposter) imposterize(original)).getRealException(), sameInstance(original));
    }

    @Test
    public void mimicsImposterizedExceptionToStringOutput() {
        original = new Exception("Detail message");
        assertThat("string representation", imposterize(original).toString(), equalTo(original.toString()));
    }

    @Test
    public void copiesImposterizedExceptionStackTrace() {
        original = new Exception("Detail message");
        original.fillInStackTrace();
        assertThat("stack trace", imposterize(original).getStackTrace(), Matchers.arrayContaining(original.getStackTrace()));
    }

    @Test
    public void mimicsImposterizedExceptionStackTraceOutput() {
        original = new Exception("Detail message");
        original.fillInStackTrace();
        assertThat("stack trace output", captureStackTrace(imposterize(original)), equalTo(captureStackTrace(original)));
    }

    private String captureStackTrace(Exception exception) {
        StringWriter capture = new StringWriter();
        exception.printStackTrace(new PrintWriter(capture));
        capture.flush();
        return capture.toString();
    }
}

