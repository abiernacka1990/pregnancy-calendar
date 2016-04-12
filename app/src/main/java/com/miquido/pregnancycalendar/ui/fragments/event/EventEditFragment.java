package com.miquido.pregnancycalendar.ui.fragments.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.miquido.pregnancycalendar.App;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.model.Event;
import com.miquido.pregnancycalendar.ui.activities.EventCreatorActivity;
import com.miquido.pregnancycalendar.ui.fragments.event.helper.EventDate;

import org.joda.time.DateTime;

public class EventEditFragment extends EventFragment {

    public static final String ARG_EVENT_START_DATE = "argEventStartDate";
    private static final String START_DATE_SIS = "START_DATE_SIS";
    private static final String END_DATE_SIS = "END_DATE_SIS";

    private EventDate startEventDate;
    private EventDate endEventDate;
    private EditText noteEditText;
    private EditText addressEditText;

    private long startDateTimeFromArg = -1;

    public EventEditFragment() {
        // Required empty public constructor
    }

    public static EventEditFragment newInstanceWithEvent(int eventId) {
        EventEditFragment fragment = new EventEditFragment();
        fragment = appendEventIdToArgs(fragment, eventId);
        return fragment;
    }

    public static EventEditFragment newInstanceWithStartDate(long startDateTimeToArg) {
        EventEditFragment fragment = new EventEditFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_EVENT_START_DATE, startDateTimeToArg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            startDateTimeFromArg = getArguments().getLong(ARG_EVENT_START_DATE, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_edit, container, false);
        initView(view, savedInstanceState);
        return view;
    }

    private void initView(View mainView, Bundle savedInstanceState) {
        findViews(mainView);
        setEventInfo(savedInstanceState);
    }

    private void setEventInfo(Bundle savedInstanceState) {

        setDates(savedInstanceState);
        if (getEvent() != null) {
            setFieldIfExist(getEvent().getNote(), noteEditText);
            setFieldIfExist(getEvent().getAddress(), addressEditText);
        }
    }

    private void setFieldIfExist(String field, EditText editText) {
        if (field != null) {
            editText.setText(field);
        }
    }

    private void setDates(Bundle savedInstanceState) {
        DateTime startDateTime;
        DateTime endDateTime;

        if (getEvent() != null) {
            startDateTime = new DateTime(getEvent().getStartDate());
            endDateTime = new DateTime(getEvent().getEndDate());
        } else if (startDateTimeFromArg > 0) {
            startDateTime = new DateTime(startDateTimeFromArg).withTime(9, 0, 0, 0);
            endDateTime = new DateTime(startDateTimeFromArg).withTime(10, 0, 0, 0);
        } else {
            startDateTime = new DateTime().withTime(9, 0, 0, 0);
            endDateTime = new DateTime().withTime(10, 0, 0, 0);
        }

        startDateTime = getDateFromSavedInstanceSteteIfExist(savedInstanceState, startDateTime, START_DATE_SIS);
        endDateTime = getDateFromSavedInstanceSteteIfExist(savedInstanceState, endDateTime, END_DATE_SIS);
        startEventDate.updateDateTime(startDateTime);
        endEventDate.updateDateTime(endDateTime);
    }

    private DateTime getDateFromSavedInstanceSteteIfExist(Bundle savedInstanceState, DateTime dateTime, String key) {
        if (savedInstanceState != null) {
            long dateTimeAsLong = savedInstanceState.getLong(key, 0);
            if (dateTimeAsLong > 0) {
                dateTime = new DateTime(dateTimeAsLong);
            }
        }
        return dateTime;
    }


    private void findViews(View mainView) {
        TextView startDateTextView = (TextView) mainView.findViewById(R.id.textview_startdate);
        TextView startTimeTextView = (TextView) mainView.findViewById(R.id.textview_starttime);
        startEventDate = new EventDate(this, startDateTextView, startTimeTextView);
        TextView endDateTextView = (TextView) mainView.findViewById(R.id.textview_enddate);
        TextView endTimeTextView = (TextView) mainView.findViewById(R.id.textview_endtime);
        endEventDate = new EventDate(this, endDateTextView, endTimeTextView);
        noteEditText = (EditText) mainView.findViewById(R.id.edittext_note);
        addressEditText = (EditText) mainView.findViewById(R.id.edittext_address);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_event_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            saveDataEndQuit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private EditText getTitleEditText() {
        EventCreatorActivity eventCreatorActivity = (EventCreatorActivity) getActivity();
        return eventCreatorActivity.getTitleEditText();
    }

    private void saveDataEndQuit() {
        Event event = createEvent();
        saveEvent(event);
        finishActivityWithResultOk();
    }

    private void saveEvent(Event event) {
        if (getEvent() == null) {
            App.getInstance().getEventsRepository().create(event);
        } else {
            App.getInstance().getEventsRepository().update(event);
        }
    }

    @Nullable
    private Event createEvent() {
        Event event = getEvent();
        Event.Builder eventBuilder = new Event.Builder();
        if (event !=  null) {
            eventBuilder.setId(event.getId());
        }
        eventBuilder.setTitle(getTitleEditText().getText().toString())
                .setStartDate(startEventDate.getDateTime().getMillis())
                .setEndDate(endEventDate.getDateTime().getMillis())
                .setNote(noteEditText.getText().toString())
                .setAddress(addressEditText.getText().toString());
        return eventBuilder.build();
    }


    public void checkDates(EventDate changedEventDate) {
        if (startEventDate.getDateTime().isAfter(endEventDate.getDateTime())) {
            fixDates(changedEventDate);
        }

    }

    private void fixDates(EventDate changedEventDate) {
        if (changedEventDate == startEventDate) {
            endEventDate.updateDateTime(startEventDate.getDateTime().plusHours(1));
        } else if (changedEventDate == endEventDate) {
            startEventDate.updateDateTime(endEventDate.getDateTime().minusHours(1));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(START_DATE_SIS, startEventDate.getDateTime().getMillis());
        outState.putLong(END_DATE_SIS, endEventDate.getDateTime().getMillis());
    }
}
