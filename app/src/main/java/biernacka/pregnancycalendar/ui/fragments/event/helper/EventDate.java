package biernacka.pregnancycalendar.ui.fragments.event.helper;

import android.widget.TextView;

import biernacka.pregnancycalendar.ui.fragments.event.EventEditFragment;
import biernacka.pregnancycalendar.utils.StringFormatter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.joda.time.DateTime;

/**
 * Created by agnieszka on 05.04.16.
 */
public class EventDate implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final boolean IS_24_HOUR_MODE = true;
    private EventEditFragment eventEditFragment;
    private DateTime dateTime;
    private final TextView dateTextView;
    private final TextView timeTextView;
    private String DATE_PICKER_DIALOG = "DATE_PICKER_DIALOG";
    private String TIME_PICKER_DIALOG = "TIME_PICKER_DIALOG";

    public EventDate(EventEditFragment eventEditFragment, TextView dateTextView, TextView timeTextView) {
        this.eventEditFragment = eventEditFragment;
        this.dateTextView = dateTextView;
        this.timeTextView = timeTextView;
        dateTextView.setOnClickListener(view -> openDateDialog());
        timeTextView.setOnClickListener(view -> openTimeDialog());
    }

    private void openTimeDialog() {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                this,
                dateTime.getHourOfDay(),
                dateTime.getMinuteOfHour(),
                IS_24_HOUR_MODE
        );
        timePickerDialog.show(eventEditFragment.getActivity().getFragmentManager(), TIME_PICKER_DIALOG);
    }


    private void openDateDialog() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                dateTime.getYear(),
                dateTime.getMonthOfYear() - 1,
                dateTime.getDayOfMonth()
        );
        datePickerDialog.show(eventEditFragment.getActivity().getFragmentManager(), DATE_PICKER_DIALOG);
    }

    public void updateDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
        dateTextView.setText(StringFormatter.withDayOfWeekDate(dateTime));
        timeTextView.setText(StringFormatter.time(dateTime));
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        DateTime newDateTime = dateTime
                .withYear(year)
                .withMonthOfYear(monthOfYear + 1)
                .withDayOfMonth(dayOfMonth);
        updateDateTime(newDateTime);
        eventEditFragment.checkDates(this);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        DateTime newDateTime = dateTime
                .withHourOfDay(hourOfDay)
                .withMinuteOfHour(minute);
        updateDateTime(newDateTime);
        eventEditFragment.checkDates(this);
    }
}
