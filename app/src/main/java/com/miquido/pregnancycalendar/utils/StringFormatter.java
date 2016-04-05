package com.miquido.pregnancycalendar.utils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by agnieszka on 03.11.15.
 */
public class StringFormatter {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");
    private static final SimpleDateFormat DATE_FORMAT_WITH_DAY_OF_WEEK = new SimpleDateFormat("EEE, dd MMM yyyy");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    public static String simpleDate(Date date) {
        return SIMPLE_DATE_FORMAT.format(date);
    }

    public static String simpleDate(long date) {
        return simpleDate(new Date(date));
    }

    public static String withDayOfWeekDate(DateTime date) {
        return DATE_FORMAT_WITH_DAY_OF_WEEK.format(date.getMillis());
    }
    public static String time(DateTime date) {
        return TIME_FORMAT.format(date.getMillis());
    }


}
