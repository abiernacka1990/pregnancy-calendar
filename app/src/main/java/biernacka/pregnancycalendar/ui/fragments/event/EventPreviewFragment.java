package biernacka.pregnancycalendar.ui.fragments.event;


import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import biernacka.pregnancycalendar.App;
import biernacka.pregnancycalendar.R;
import biernacka.pregnancycalendar.utils.StringFormatter;

import org.joda.time.DateTime;

public class EventPreviewFragment extends EventFragment {


    public EventPreviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_event_preview, container, false);
        initView(mainView);
        return mainView;
    }

    private void initView(View mainView) {
        initDateTime(mainView);
        setFieldIfExist(mainView, getEvent().getAddress(), R.id.view_address, R.id.textview_address);
        setFieldIfExist(mainView, getEvent().getNote(), R.id.view_note, R.id.textview_note);
    }

    private void setFieldIfExist(View mainView, String field, int viewResId, int textviewResId) {
        View fieldView = mainView.findViewById(viewResId);
        if (field != null && !field.isEmpty()) {
            fieldView.setVisibility(View.VISIBLE);
            TextView textView = (TextView) mainView.findViewById(textviewResId);
            textView.setText(field);
        } else {
            fieldView.setVisibility(View.GONE);
        }
    }

    private void initDateTime(View mainView) {
        TextView startDateTextView = (TextView) mainView.findViewById(R.id.textview_startdate);
        TextView startTimeTextView = (TextView) mainView.findViewById(R.id.textview_starttime);
        long startDateAsLong = getEvent().getStartDate();
        setDateTimeText(startDateTextView, startTimeTextView, startDateAsLong);
        TextView endDateTextView = (TextView) mainView.findViewById(R.id.textview_enddate);
        TextView endTimeTextView = (TextView) mainView.findViewById(R.id.textview_endtime);
        long endDateAsLong = getEvent().getEndDate();
        setDateTimeText(endDateTextView, endTimeTextView, endDateAsLong);
    }

    private void setDateTimeText(TextView dateTextView, TextView timeTextView, long dateAsLong) {
        DateTime date = new DateTime(dateAsLong);
        String formattedDate = StringFormatter.withDayOfWeekDate(date);
        String formattedTime = StringFormatter.time(date);
        dateTextView.setText(formattedDate);
        timeTextView.setText(formattedTime);
    }

    public static EventPreviewFragment newInstance(int argEventId) {
        EventPreviewFragment fragment = new EventPreviewFragment();
        fragment = appendEventIdToArgs(fragment, argEventId);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_event_preview, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            onActionDelete();
            return true;
        } else if (item.getItemId() == R.id.action_add_to_calendar) {
            onActionAddToCalendar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onActionDelete() {
        App.getInstance().getEventsRepository().delete(getEvent());
        finishActivityWithResultOk();
    }

    private void onActionAddToCalendar() {
        Intent insertToCalendarIntent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, getEvent().getStartDate())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, getEvent().getEndDate())
                .putExtra(CalendarContract.Events.TITLE, getEvent().getTitle())
                .putExtra(CalendarContract.Events.DESCRIPTION, getEvent().getNote())
                .putExtra(CalendarContract.Events.EVENT_LOCATION, getEvent().getAddress());
        startActivity(insertToCalendarIntent);
    }

}