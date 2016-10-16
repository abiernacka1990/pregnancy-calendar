package biernacka.pregnancycalendar.utils;

import android.support.annotation.Nullable;

import org.joda.time.DateTime;

import biernacka.pregnancycalendar.ui.helpers.PregnancyDatesHelper;

/**
 * Created by agnieszka on 05.07.16.
 */
public final class DatesHelper {

    public static DateTime today() {
        return new DateTime().withTimeAtStartOfDay();
    }

    @Nullable
    public static Integer getCurrentWeek() {
        Integer currentWeek = null;
        if (PregnancyDatesHelper.isInPregnancyTime(today().getMillis())) {
            currentWeek = PregnancyDatesHelper.getWeek(today().getMillis());
        }
        return currentWeek;
    }

    public static boolean currentTimeIsInPregnancy() {
        return PregnancyDatesHelper.isInPregnancyTime(today().getMillis());
    }
}
