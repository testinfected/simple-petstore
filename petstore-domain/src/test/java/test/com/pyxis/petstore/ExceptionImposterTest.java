package test.com.pyxis.petstore;

import com.pyxis.petstore.ExceptionImposter;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class ExceptionImposterTest {
    private Exception realException;

    @Test
    public void leavesUncheckedExceptionsUnchanged() {
        realException = new RuntimeException();
        assertSame(realException, ExceptionImposter.imposterize(realException));
    }

    @Test
    public void imposterizesCheckedExceptionsAndKeepsAReference() {
        realException = new Exception();
        RuntimeException imposter = ExceptionImposter.imposterize(realException);
        assertTrue(imposter instanceof ExceptionImposter);
        assertSame(realException, ((ExceptionImposter) imposter).getRealException());
    }

    @Test
    public void mimicsImposterizedExceptionToStringOutput() {
        realException = new Exception("Detail message");
        RuntimeException imposter = ExceptionImposter.imposterize(realException);
        assertEquals(realException.toString(), imposter.toString());
    }

    @Test
    public void copiesImposterizedExceptionStackTrace() {
        realException = new Exception("Detail message");
        realException.fillInStackTrace();
        RuntimeException imposter = ExceptionImposter.imposterize(realException);
        assertArrayEquals(realException.getStackTrace(), imposter.getStackTrace());
    }

    @Test
    public void mimicsImposterizedExceptionStackTraceOutput() {
        realException = new Exception("Detail message");
        realException.fillInStackTrace();
        RuntimeException imposter = ExceptionImposter.imposterize(realException);
        assertEquals(stackTraceOf(realException), stackTraceOf(imposter));
    }

    private String stackTraceOf(Exception exception) {
        StringWriter capture = new StringWriter();
        exception.printStackTrace(new PrintWriter(capture));
        capture.flush();
        return capture.toString();
    }
}
