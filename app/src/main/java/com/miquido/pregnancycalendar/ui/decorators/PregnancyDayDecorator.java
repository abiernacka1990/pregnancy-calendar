package com.miquido.pregnancycalendar.ui.decorators;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.miquido.pregnancycalendar.App;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.model.Event;
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
        if (eventsForDay != null && !eventsForDay.isEmpty()) {
            drawCircleInCell(cell);
        }
    }

    private void drawCircleInCell(@NonNull DayView cell) {
        Drawable drawable = ContextCompat.getDrawable(cell.getContext(), R.drawable.calendar_circle);
        cell.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
    }
}
