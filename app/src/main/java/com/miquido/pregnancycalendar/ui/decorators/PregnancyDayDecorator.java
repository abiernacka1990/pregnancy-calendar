package com.miquido.pregnancycalendar.ui.decorators;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;

import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.utils.Preferences;
import com.miquido.pregnancycalendar.utils.StringFormatter;
import com.samsistemas.calendarview.decor.DayDecorator;
import com.samsistemas.calendarview.widget.DayView;

import org.joda.time.LocalDate;

import java.util.Date;

/**
 * Created by agnieszka on 22.03.16.
 */
public class PregnancyDayDecorator implements DayDecorator {

    private static final long WEEK_TIME_IN_MILLIS = 7 * 24 * 60 * 60 * 1000;

    @Override
    public void decorate(@NonNull DayView cell) {

        Date pregnancyStartDate = getPregnancyStartDate();

        if (pregnancyStartDate != null) {
  //          updateCellText(cell, pregnancyStartDate);
        }
    }

    public Date getPregnancyStartDate() {
        long pregnancyStartDate = Preferences.getInstance().getPregnancyStartDate();
        if (pregnancyStartDate > 0) {
            return new Date(pregnancyStartDate);
        } else {
            return null;
        }
    }

    private void updateCellText(DayView cell, Date pregnancyStartDate) {

        String dayText = cell.getText().toString();
        long pregnancyStartDateWithoutTime = gateDateWithoutTimeInMillis(pregnancyStartDate);
        long cellDateWithoutTime = gateDateWithoutTimeInMillis(cell.getDate());
        long timeDiff = getTimeDiff(pregnancyStartDateWithoutTime, cellDateWithoutTime);
        Drawable drawable = ContextCompat.getDrawable(cell.getContext(), R.drawable.calendar_circle);
        cell.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

        if (isInPregnancyTime(timeDiff)) {
            String newDateText = dayText + extraTextAboutWeek(timeDiff);
            Spannable span = new SpannableString(newDateText);
            span.setSpan(new RelativeSizeSpan(0.5f), dayText.length(), newDateText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            cell.setText(span);
        }
    }

    private boolean isInPregnancyTime(long timeDiff) {
        return timeDiff >=0 && timeDiff <= (WEEK_TIME_IN_MILLIS * 40);
    }

    private String extraTextAboutWeek(long timeDiff) {
        if (timeDiff >= 0) {
            if (timeDiff % WEEK_TIME_IN_MILLIS == 0) {
                long week = timeDiff / WEEK_TIME_IN_MILLIS;
                return String.format("\n(%d)", week);
            }
        }
        return "";
    }

    private long getTimeDiff(long pregnancyStartDate, long cellDate) {
        return cellDate - pregnancyStartDate;
    }

    //FIXME: add JodaTime
    private long gateDateWithoutTimeInMillis(Date date) {
        LocalDate dateWithoutTimeInMillis = new LocalDate(date);
        return dateWithoutTimeInMillis.toDate().getTime();
    }


}
