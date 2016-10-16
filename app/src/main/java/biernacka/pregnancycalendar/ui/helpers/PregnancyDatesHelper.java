package biernacka.pregnancycalendar.ui.helpers;

import org.joda.time.DateTime;

import biernacka.pregnancycalendar.utils.DatesHelper;
import biernacka.pregnancycalendar.utils.Preferences;

/**
 * Created by agnieszka on 30.03.16.
 */
public final class PregnancyDatesHelper {

    private static final long WEEK_TIME_IN_MILLIS = 7 * 24 * 60 * 60 * 1000;
    public static final int ALL_PREGNANCY_WEEKS = 40;

    public static int getWeek(long date) {

        DateTime dateTime = new DateTime(date).withTimeAtStartOfDay();

        long timeDiff = getTimeDiff(getPlannedBirthDate(), dateTime.getMillis());

        if (isInPregnancyTime(dateTime.getMillis())) {
            int week = ALL_PREGNANCY_WEEKS - (int) (timeDiff / WEEK_TIME_IN_MILLIS);
            return week;
        } else {
            throw new RuntimeException("Cannot calculate week for specified date.");
        }
    }

    public static boolean isTodayPregnancyTime() {
        return isInPregnancyTime(DatesHelper.today().getMillis());
    }

    public static boolean isInPregnancyTime(long date) {

        if(!isPlannedBirthDateSet()) {
            return false;
        }

        DateTime dateTime = new DateTime(date).withTimeAtStartOfDay();
        long timeDiff = getTimeDiff(getPlannedBirthDate(), dateTime.getMillis());
        return timeDiff >=0 && timeDiff <= (WEEK_TIME_IN_MILLIS * ALL_PREGNANCY_WEEKS);
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
