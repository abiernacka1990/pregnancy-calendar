package com.miquido.pregnancycalendar.ui.helpers;

import com.miquido.pregnancycalendar.utils.Preferences;

import java.util.Date;

/**
 * Created by agnieszka on 30.03.16.
 */
public final class PregnancyDatesHelper {

    private static final long WEEK_TIME_IN_MILLIS = 7 * 24 * 60 * 60 * 1000;

    public static int getWeek(long date) {

        long timeDiff = getTimeDiff(date, getPregnancyStartDate());

        if (isInPregnancyTime(date)) {
            int week = (int) (timeDiff / WEEK_TIME_IN_MILLIS);
            return week;
        } else {
            throw new RuntimeException("Cannot calculate week for specified date.");
        }
    }

    public static  boolean isInPregnancyTime(long date) {

        if(!isPregnancyStartDateSet()) {
            return false;
        }

        long timeDiff = getTimeDiff(date, getPregnancyStartDate());
        return timeDiff >=0 && timeDiff <= (WEEK_TIME_IN_MILLIS * 40);
    }

    private static boolean isPregnancyStartDateSet() {
        return getPregnancyStartDate() > 0;
    }

    private static long getTimeDiff(long date, long pregnancyStartDate) {
        return date - pregnancyStartDate;
    }

    private static long getPregnancyStartDate() {
        return Preferences.getInstance().getPregnancyStartDate();
    }

}
