package org.testinfected.petstore.lib;

import java.io.OutputStream;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class ConsoleHandler extends StreamHandler {

    public static ConsoleHandler toStandardOutput() {
        return new ConsoleHandler(System.out);
    }

    public ConsoleHandler(OutputStream out) {
        super(out, new PlainFormatter());
    }

    public void publish(LogRecord record) {
        super.publish(record);
        flush();
    }

    public void close() throws SecurityException {
        flush();
    }
}
