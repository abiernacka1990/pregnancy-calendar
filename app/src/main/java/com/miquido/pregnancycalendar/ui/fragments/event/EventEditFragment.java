package com.miquido.pregnancycalendar.ui.fragments.event;

import android.app.Activity;
import android.os.Bundle;
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
import com.miquido.pregnancycalendar.utils.StringFormatter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;

import validation.Validator;
import validation.fields.EventTitleEditTextValidator;

public class EventEditFragment extends EventFragment {

    private static final String START_DATE_PICKER_DIALOG = "START_DATE_PICKER_DIALOG";
    public static final String ARG_EVENT_START_DATE = "argEventStartDate";

    private TextView startDateTextView;
    private DateTime startDateTime;
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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            startDateTimeFromArg = getArguments().getLong(ARG_EVENT_START_DATE, -1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_edit, container, false);
        boolean shouldSetEventInfo = savedInstanceState == null;
        initView(view, shouldSetEventInfo);
        return view;
    }

    private void initView(View mainView, boolean shouldSetEventInfo) {
        findViews(mainView);
        setListeners();
        if (shouldSetEventInfo) {
            setEventInfo();
        }
    }

    private void setEventInfo() {
        if (getEvent() != null) {
            startDateTime = new DateTime(getEvent().getDate());
        } else if (startDateTimeFromArg > 0) {
            startDateTime = new DateTime(startDateTimeFromArg).withTime(9, 0, 0, 0);
        } else {
            startDateTime = new DateTime().withTime(9, 0, 0, 0);
        }
        updateDate(startDateTextView, startDateTime);
    }

    private void setListeners() {
        startDateTextView.setOnClickListener(view -> openDateDialog());
    }

    private void findViews(View mainView) {
        startDateTextView = (TextView) mainView.findViewById(R.id.textview_startdate);
    }

    private void openDateDialog() {
        DatePickerDialog startPregnancyDatePickerDialog = DatePickerDialog.newInstance(
                onStartDateSelectedListener,
                startDateTime.getYear(),
                startDateTime.getMonthOfYear() - 1,
                startDateTime.getDayOfMonth()
        );
        startPregnancyDatePickerDialog.show(getActivity().getFragmentManager(), START_DATE_PICKER_DIALOG);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_event_creator, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            validDataAndSave();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private EditText getTitleEditText() {
        EventCreatorActivity eventCreatorActivity = (EventCreatorActivity) getActivity();
        return eventCreatorActivity.getTitleEditText();
    }

    private void validDataAndSave() {
        if (validData()) {
            createEventAndSave();
            finishActivity();
        }
    }

    private void finishActivity() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    private void createEventAndSave() {
        Event event = getEvent();
        if (event == null) {
            event = new Event();
        }
        event.setTitle(getTitleEditText().getText().toString());
        event.setDate(startDateTime.getMillis());
        if (getEvent() == null) {
            App.getInstance().getEventsRepository().create(event);
        } else {
            App.getInstance().getEventsRepository().update(event);
        }
    }

    private boolean validData() {
        Validator validator = Validator.create(new EventTitleEditTextValidator(getTitleEditText()));
        return validator.validate();
    }

    private DatePickerDialog.OnDateSetListener onStartDateSelectedListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            startDateTime = startDateTime
                    .withYear(year)
                    .withMonthOfYear(monthOfYear + 1)
                    .withDayOfMonth(dayOfMonth);
            updateDate(startDateTextView, startDateTime);
        }
    };

    private void updateDate(TextView textView, DateTime dateTime) {
        textView.setText(StringFormatter.withDayOfWeekDate(dateTime));
    }
}
