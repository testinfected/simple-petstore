package org.testinfected.petstore.lib;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class PlainFormatter extends Formatter {
    public String format(LogRecord record) {
        return record.getMessage() + "\n";
    }
}