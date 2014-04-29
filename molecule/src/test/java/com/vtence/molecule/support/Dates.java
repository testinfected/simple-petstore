package com.vtence.molecule.support;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Dates {

    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    private TimeZone timeZone = GMT;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private int millisecond;
    private String name;

    public static Dates aDate() {
        return now();
    }

    public static Dates now() {
        return new Dates().at(System.currentTimeMillis());
    }

    public static Dates namedDate(String name) {
        return aDate().named(name);
    }

    public static Dates calendarDate(int year, int month, int day) {
        return aDate().onCalendar(year, month, day);
    }

    public Dates inZone(String zone) {
        return in(TimeZone.getTimeZone(zone));
    }

    public Dates in(TimeZone zone) {
        timeZone = zone;
        return this;
    }

    public Dates onCalendar(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        return this;
    }

    public Dates atTime(int hour, int minute, int second) {
        return atTime(hour, minute, second, 0);
    }

    public Dates atTime(int hour, int minute, int second, int millisecond) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.millisecond = millisecond;
        return this;
    }

    public Dates atMidnight() {
        return atTime(0, 0, 0);
    }

    public Dates at(long pointInTime) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTimeInMillis(pointInTime);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
        millisecond = calendar.get(Calendar.MILLISECOND);
        return this;
    }

    public Dates named(String name) {
        this.name = name;
        return this;
    }

    public Dates but() {
        return new Dates().at(toMillis()).named(name);
    }

    public Date toDate() {
        return name != null ? new Date(toMillis()) {
            public String toString() {
                return name;
            }
        } : new Date(toMillis());
    }

    public long toMillis() {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        return calendar.getTimeInMillis();
    }
}