package test.integration.com.pyxis.petstore.persistence.support;

import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class ExceptionImposterTest
{
    private Exception original;

    @Test
    public void leavesUncheckedExceptionsUnchanged()
    {
        original = new RuntimeException();
        assertSame( original, ExceptionImposter.imposterize( original ));
    }

    @Test
    public void imposterizesCheckedExceptionsAndKeepsAReference()
    {
        original = new Exception();
        RuntimeException imposter = ExceptionImposter.imposterize( original );
        assertTrue( imposter instanceof ExceptionImposter );
        assertSame( original, ((ExceptionImposter) imposter).getRealException() );
    }

    @Test
    public void mimicsImposterizedExceptionToStringOutput()
    {
        original = new Exception( "Detail message" );
        RuntimeException imposter = ExceptionImposter.imposterize( original );
        assertEquals( original.toString(), imposter.toString() );
    }

    @Test
    public void copiesImposterizedExceptionStackTrace()
    {
        original = new Exception( "Detail message" );
        original.fillInStackTrace();
        RuntimeException imposter = ExceptionImposter.imposterize( original );
        assertArrayEquals( original.getStackTrace(), imposter.getStackTrace() );
    }

    @Test
    public void mimicsImposterizedExceptionStackTraceOutput()
    {
        original = new Exception( "Detail message" );
        original.fillInStackTrace();
        RuntimeException imposter = ExceptionImposter.imposterize( original );
        assertEquals( captureStackTrace( original ), captureStackTrace( imposter ) );
    }

    private String captureStackTrace(Exception exception)
    {
        StringWriter capture = new StringWriter();
        exception.printStackTrace( new PrintWriter( capture ) );
        capture.flush();
        return capture.toString();
    }
}
