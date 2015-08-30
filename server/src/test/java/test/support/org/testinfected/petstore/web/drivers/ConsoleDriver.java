package test.support.org.testinfected.petstore.web.drivers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ConsoleDriver {

    private final PrintStream output = new PrintStream(new ByteArrayOutputStream());
    private PrintStream standardOut;

    public void capture() {
        standardOut = System.out;
        System.setOut(output);
    }

    public void release() {
        System.setOut(standardOut);
    }
}