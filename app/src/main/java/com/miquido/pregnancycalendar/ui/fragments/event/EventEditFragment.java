package com.miquido.pregnancycalendar.ui.fragments.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.miquido.pregnancycalendar.App;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.model.Event;
import com.miquido.pregnancycalendar.ui.activities.EventCreatorActivity;

import validation.Validator;
import validation.fields.EventTitleEditTextValidator;

public class EventEditFragment extends EventFragment {

    public EventEditFragment() {
        // Required empty public constructor
    }

    public static EventEditFragment newInstance() {
        return new EventEditFragment();
    }

    public static EventEditFragment newInstance(int eventId) {
        EventEditFragment fragment = new EventEditFragment();
        fragment = appendEventIdToArgs(fragment, eventId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_edit, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_event_creator, menu);
        super.onCreateOptionsMenu(menu,inflater);
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
        getActivity().finish();
    }

    private void createEventAndSave() {
        Event event = new Event();
        event.setTitle(getTitleEditText().getText().toString());
        App.getInstance().getEventsRepository().create(event);
    }

    private boolean validData() {
        Validator validator = Validator.create(new EventTitleEditTextValidator(getTitleEditText()));
        return validator.validate();
    }
}
