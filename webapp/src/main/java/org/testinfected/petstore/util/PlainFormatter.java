package org.testinfected.petstore.util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class PlainFormatter extends Formatter {
    public String format(LogRecord record) {
        return record.getMessage() + "\n";
    }
}