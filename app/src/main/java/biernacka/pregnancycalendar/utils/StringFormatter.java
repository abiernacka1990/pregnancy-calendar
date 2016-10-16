package biernacka.pregnancycalendar.utils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by agnieszka on 03.11.15.
 */
public class StringFormatter {

    private static SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
    }

    private static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
    }

    private static SimpleDateFormat getTimeFormat() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    private static SimpleDateFormat getEventsHeaderDateFormat() {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    }

    private static SimpleDateFormat getDateFormatWithDayOfWeek() {
        return new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());
    }


    public static String simpleDate(Date date) {
        return getSimpleDateFormat().format(date);
    }

    public static String date(Date date) {
        return getDateFormat().format(date);
    }

    public static String simpleDate(long date) {
        return simpleDate(new Date(date));
    }

    public static String withDayOfWeekDate(DateTime date) {
        return getDateFormatWithDayOfWeek().format(date.getMillis());
    }

    public static String time(DateTime date) {
        return getTimeFormat().format(date.getMillis());
    }


    public static String eventHeaderFormat(long date) {
        return getEventsHeaderDateFormat().format(date);
    }
}
