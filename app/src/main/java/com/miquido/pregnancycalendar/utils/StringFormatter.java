package com.miquido.pregnancycalendar.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by agnieszka on 03.11.15.
 */
public class StringFormatter {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

    public static String date(Date date) {
        return sdf.format(date);
    }

    public static String date(long date) {
        return date(new Date(date));
    }


}
