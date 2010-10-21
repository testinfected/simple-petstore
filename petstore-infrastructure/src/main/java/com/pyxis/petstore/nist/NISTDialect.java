package com.pyxis.petstore.nist;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class NISTDialect implements TimeServerDialect {
    private static final DateFormat NIST_DATE_TIME_FORMAT = inUTC("yy-MM-dd HH:mm:ss");

    private static DateFormat inUTC(String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

    public Date translate(String serverOutput) throws ParseException {
        return NIST_DATE_TIME_FORMAT.parse(dateTimePart(serverOutput));
    }

    private String dateTimePart(String serverOutput) {
        return serverOutput.substring(6, 23);
    }
}
