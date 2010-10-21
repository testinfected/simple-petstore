package test.support.com.pyxis.petstore.builders;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateBuilder {

    private TimeZone gmt = TimeZone.getTimeZone("Universal");

    private TimeZone timeZone = gmt;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private int millisecond;

    public static DateBuilder aDate() {
        DateBuilder builder = new DateBuilder();
        return builder.inMillis(System.currentTimeMillis());
    }

    public DateBuilder at(int year, int month, int day, int hour, int minute, int second) {
        return onCalendar(year, month, day).atTime(hour, minute, second);
    }

    public DateBuilder in(TimeZone zone) {
        timeZone = zone;
        return this;
    }

    public DateBuilder onCalendar(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        return this;
    }

    public DateBuilder atTime(int hour, int minute, int second) {
        return atTime(hour, minute, second, 0);
    }

    public DateBuilder atTime(int hour, int minute, int second, int millisecond) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.millisecond = millisecond;
        return this;
    }

    public DateBuilder atMidnight() {
        return atTime(0, 0, 0);
    }

    public DateBuilder inMillis(long millis) {
        Calendar calendar = Calendar.getInstance(gmt);
        calendar.setTimeInMillis(millis);
        timeZone = calendar.getTimeZone();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
        millisecond = calendar.get(Calendar.MILLISECOND);
        return this;
    }

    public Date build() {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        return calendar.getTime();
    }
}