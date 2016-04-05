package com.miquido.pregnancycalendar.ui.decorators;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.miquido.pregnancycalendar.App;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.model.Event;
import com.miquido.pregnancycalendar.utils.Preferences;
import com.samsistemas.calendarview.decor.DayDecorator;
import com.samsistemas.calendarview.widget.DayView;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

/**
 * Created by agnieszka on 22.03.16.
 */
public class PregnancyDayDecorator implements DayDecorator {

    @Override
    public void decorate(@NonNull DayView cell) {
        long date = cell.getDate().getTime();
        List<Event> eventsForDay = App.getInstance().getEventsRepository().getAllEventsForSpecifiedDay(date);
        cell.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        if (eventsForDay != null && !eventsForDay.isEmpty()) {
            drawIconBottom(cell, R.drawable.calendar_circle);
        }
        if (isStartOfPregnancy(cell.getDate())) {
            drawIconBottom(cell, R.drawable.ic_heart);
        }
    }

    private boolean isStartOfPregnancy(Date date) {
        DateTime pregnancyDate = new DateTime(Preferences.getInstance().getPregnancyStartDate()).withTime(0, 0, 0, 0);
        DateTime cellDate = new DateTime(date).withTime(0, 0, 0, 0);
        if(pregnancyDate.isEqual(cellDate)) {
            return true;
        }
        return false;

    }

    private void drawIconBottom(@NonNull DayView cell, int drawableRes) {
        Drawable drawable = ContextCompat.getDrawable(cell.getContext(), drawableRes);
        cell.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
    }
}
