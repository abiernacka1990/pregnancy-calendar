package biernacka.pregnancycalendar.ui.fragments.main.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.Date;

import biernacka.pregnancycalendar.R;
import biernacka.pregnancycalendar.ui.helpers.PregnancyDatesHelper;
import biernacka.pregnancycalendar.utils.Preferences;
import biernacka.pregnancycalendar.utils.StringFormatter;

/**
 * Created by agnieszka on 13.04.16.
 */
public class DashboardFragment extends BaseDashboardFragment {

    private static final long DAY_AS_MILLIS = 1000 * 60 * 60 * 24;
    private TextView dateTextView;
    private TextView calendInfoTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstancState) {

        View mainView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initView(mainView);
        return mainView;
    }

    private void initView(View mainView) {
        findViews(mainView);
        setViewsParameters();
    }

    private void findViews(View mainView) {
        dateTextView = (TextView) mainView.findViewById(R.id.textview_date);
        calendInfoTextView = (TextView) mainView.findViewById(R.id.textview_calend_info);
    }

    private void setViewsParameters() {
        setCurrentDate();
        setCalendarInfo();
    }

    private void setCurrentDate() {
        String currentDate = StringFormatter.date(new Date());
        dateTextView.setText(currentDate);
    }

    private void setCalendarInfo() {
        String calendarInfo = new CalendarInfo().getInfoText();
        calendInfoTextView.setText(calendarInfo);

    }


    private class CalendarInfo {

        private final DateTime today;
        private final long planndedBirthDate;
        private final Integer currentWeek;
        private final long daysToOrAfterPlannedBirthDate;

        public CalendarInfo() {
            today = new DateTime().withTimeAtStartOfDay();
            planndedBirthDate = Preferences.getInstance().getPlannedBirthDate();
            currentWeek = getCurrentWeek(today);
            daysToOrAfterPlannedBirthDate =  Math.abs((today.getMillis() - planndedBirthDate) / DAY_AS_MILLIS);
        }

        @Nullable
        private Integer getCurrentWeek(DateTime now) {
            Integer currentWeek = null;
            if (PregnancyDatesHelper.isInPregnancyTime(now.getMillis())) {
                currentWeek = PregnancyDatesHelper.getWeek(now.getMillis());
            }
            return currentWeek;
        }

        public String getInfoText() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(");

            appendWeekInfo(stringBuilder);
            appendDayInfo(stringBuilder);

            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        private void appendDayInfo(StringBuilder stringBuilder) {
            String dayInfo = getString(R.string.dashboard_calend_info_day_info);
            String validFormOfDay = getValidFormOfDay(daysToOrAfterPlannedBirthDate);
            String afterOrBefore = getAfterOrBeforePlannedBirthDate();
            stringBuilder.append(String.format(dayInfo, daysToOrAfterPlannedBirthDate, validFormOfDay, afterOrBefore));
        }

        private void appendWeekInfo(StringBuilder stringBuilder) {
            if (currentWeek != null) {
                String weekInfo = getString(R.string.dashboard_calend_info_week_info);
                String validFormOfWeek = getValidFormOfWeek(currentWeek);
                stringBuilder.append(String.format(weekInfo, currentWeek.intValue(), validFormOfWeek));
            }
        }

        private String getValidFormOfWeek(Integer currentWeek) {
            return currentWeek > 1 ? getString(R.string.dashboard_calend_info_weeks) : getString(R.string.dashboard_calend_info_week);
        }

        private String getValidFormOfDay(long day) {
            return day > 1 ? getString(R.string.dashboard_calend_info_days) : getString(R.string.dashboard_calend_info_day);
        }

        private String getAfterOrBeforePlannedBirthDate() {
            String afterOrBeforePlannedBirthDate;
            if (planndedBirthDate < today.getMillis()) {
                afterOrBeforePlannedBirthDate = getString(R.string.dashboard_calend_info_after);
            } else {
                afterOrBeforePlannedBirthDate = getString(R.string.dashboard_calend_info_before);
            }
            return afterOrBeforePlannedBirthDate;
        }
    }
}
