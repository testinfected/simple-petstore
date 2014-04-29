package com.vtence.molecule.http;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Parsing and formatting of HTTP dates as used in cookies and other headers.
 * <p>
 * This class handles dates as defined by RFC 2616 section 3.3.1
 */
public class HttpDate {

    public static final String RFC_1123_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final String RFC_1036_FORMAT = "EEE, dd-MMM-yy HH:mm:ss zzz";
    public static final String ANSI_ASCTIME_FORMAT = "EEE MMM d HH:mm:ss yyyy";

    private static final String[] POSSIBLE_FORMATS = new String[] {
            RFC_1123_FORMAT, RFC_1036_FORMAT, ANSI_ASCTIME_FORMAT
    };
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    public static Date parse(String value) {
        for (String format : POSSIBLE_FORMATS) {
            Date date = parse(value, format);
            if (date != null) return date;

        }
        throw new IllegalArgumentException("Invalid date format: " + value);
    }

    private static Date parse(String date, String format) {
        SimpleDateFormat httpDate = new SimpleDateFormat(format, Locale.US);
        httpDate.setTimeZone(GMT);
        return httpDate.parse(date, new ParsePosition(0));
    }

    public static String format(long date) {
        return format(new Date(date));
    }

    public static String format(Date date) {
        return rfc1123(date);
    }

    public static String rfc1123(Date date) {
        SimpleDateFormat httpDate = new SimpleDateFormat(RFC_1123_FORMAT, Locale.US);
        httpDate.setTimeZone(GMT);
        return httpDate.format(date);
    }
}
