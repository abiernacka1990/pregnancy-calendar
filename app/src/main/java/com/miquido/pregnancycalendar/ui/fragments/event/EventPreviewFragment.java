package com.miquido.pregnancycalendar.ui.fragments.event;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.utils.StringFormatter;

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

}
