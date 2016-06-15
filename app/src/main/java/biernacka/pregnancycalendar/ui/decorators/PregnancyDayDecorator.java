package biernacka.pregnancycalendar.ui.decorators;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import biernacka.pregnancycalendar.App;
import biernacka.pregnancycalendar.R;
import biernacka.pregnancycalendar.model.Event;
import biernacka.pregnancycalendar.utils.Preferences;
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
        if (isBirthDate(cell.getDate())) {
            drawIconBottom(cell, R.drawable.ic_heart);
        }
    }

    private boolean isBirthDate(Date date) {
        DateTime birthDate = new DateTime(Preferences.getInstance().getPlannedBirthDate()).withTime(0, 0, 0, 0);
        DateTime cellDate = new DateTime(date).withTime(0, 0, 0, 0);
        if(birthDate.isEqual(cellDate)) {
            return true;
        }
        return false;

    }

    private void drawIconBottom(@NonNull DayView cell, int drawableRes) {
        Drawable drawable = ContextCompat.getDrawable(cell.getContext(), drawableRes);
        cell.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
    }
}
