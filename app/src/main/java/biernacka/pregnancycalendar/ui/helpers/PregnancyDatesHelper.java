package biernacka.pregnancycalendar.ui.helpers;

import biernacka.pregnancycalendar.utils.Preferences;

/**
 * Created by agnieszka on 30.03.16.
 */
public final class PregnancyDatesHelper {

    private static final long WEEK_TIME_IN_MILLIS = 7 * 24 * 60 * 60 * 1000;
    public static final int ALL_PREGNANCY_WEEKS = 40;

    public static int getWeek(long date) {

        long timeDiff = getTimeDiff(getPlannedBirthDate(), date);

        if (isInPregnancyTime(date)) {
            int week = ALL_PREGNANCY_WEEKS - (int) (timeDiff / WEEK_TIME_IN_MILLIS);
            return week;
        } else {
            throw new RuntimeException("Cannot calculate week for specified date.");
        }
    }

    public static  boolean isInPregnancyTime(long date) {

        if(!isPlannedBirthDateSet()) {
            return false;
        }

        long timeDiff = getTimeDiff(getPlannedBirthDate(), date);
        return timeDiff >=0 && timeDiff <= (WEEK_TIME_IN_MILLIS * 40);
    }

    private static boolean isPlannedBirthDateSet() {
        return getPlannedBirthDate() > 0;
    }

    private static long getTimeDiff(long date1, long date2) {
        return date1 - date2;
    }

    private static long getPlannedBirthDate() {
        return Preferences.getInstance().getPlannedBirthDate();
    }

}
