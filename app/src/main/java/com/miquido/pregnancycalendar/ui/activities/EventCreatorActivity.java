package com.miquido.pregnancycalendar.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.miquido.pregnancycalendar.App;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.model.Event;
import com.miquido.pregnancycalendar.ui.fragments.event.EventEditFragment;
import com.miquido.pregnancycalendar.ui.fragments.event.EventPreviewFragment;

import java.util.Calendar;

public class EventCreatorActivity extends AppCompatActivity {

    public static String ARG_EVENT_ID = "ARG_EVENT_ID";
    private static final int NO_ID_FOUND_VALUE = -1;

    private Integer argEventId;
    private Event event;

    private Mode mode;

    private TextView headerTitleTextView;
    private EditText headerTitleEditText;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creator);

        loadBundleArgs();

        initView();
        setMode();
        showFragmentForMode(savedInstanceState, mode);
        updateViewForSpecifiedMode();

    }

    private void initView() {
        initToolbar();
        initFab();
        initHeader();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_delete);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initHeader() {
        headerTitleTextView = (TextView) findViewById(R.id.textview_title);
        headerTitleEditText = (EditText) findViewById(R.id.edittext_title);
    }

    private void initFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab_edit);
        assert fab != null;
        fab.setOnClickListener(view -> startEdit());
    }

    private void loadBundleArgs() {
        if (getIntent() != null) {
            int eventIdFromBundle = getIntent().getIntExtra(ARG_EVENT_ID, NO_ID_FOUND_VALUE);
            if (eventIdFromBundle != NO_ID_FOUND_VALUE) {
                argEventId = eventIdFromBundle;
                event = App.getInstance().getEventsRepository().get(argEventId);
            }
        }
    }

    private void setMode() {
        if (mode == null) {
            if (checkIntentIfItsNewEvent()) {
                mode = Mode.EDIT;
            } else {
                mode = Mode.PREVIEW;
            }
        }
    }

    private boolean checkIntentIfItsNewEvent() {
        return event == null;
    }

    private void showFragmentForMode(Bundle savedInstanceState, Mode mode) {

        if (savedInstanceState == null) {
            Fragment fragmentToShow = null;
            switch (mode) {
                case EDIT:
                    fragmentToShow = EventEditFragment.newInstanceWithStartDate(getStartDateFromIntentOrToday());
                    break;
                case PREVIEW:
                    fragmentToShow = EventPreviewFragment.newInstance(argEventId);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragmentToShow).commit();
        }
    }

    private long getStartDateFromIntentOrToday() {
        if (getIntent() != null) {
            long startDateFromArg = getIntent().getLongExtra(EventEditFragment.ARG_EVENT_START_DATE, -1);
            if (startDateFromArg > 0) {
                return startDateFromArg;
            }
        }
        return Calendar.getInstance().getTimeInMillis();
    }

    private void updateViewForSpecifiedMode() {

        assert mode != null;

        int editViewsVisibility = View.GONE;
        int previewViewsVisibility = View.GONE;

        switch (mode) {
            case EDIT:
                editViewsVisibility = View.VISIBLE;
                break;
            case PREVIEW:
                previewViewsVisibility = View.VISIBLE;
                break;
        }

        setViewsVisibility(editViewsVisibility, previewViewsVisibility);
        if (event != null) {
            setEventInfo();
        }
    }

    private void setViewsVisibility(int editViewsVisibility, int previewViewsVisibility) {
        headerTitleEditText.setVisibility(editViewsVisibility);
        headerTitleTextView.setVisibility(previewViewsVisibility);
        fab.setVisibility(previewViewsVisibility);
    }

    private void setEventInfo() {
        String title = event.getEventTitle(this);
        headerTitleTextView.setText(title);
        headerTitleEditText.setText(title);
    }


    private void startEdit() {
        mode = Mode.EDIT;
        updateViewForSpecifiedMode();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, EventEditFragment.newInstanceWithEvent(argEventId))
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public EditText getTitleEditText() {
        return headerTitleEditText;
    }

    private enum Mode {
        EDIT, PREVIEW
    }
}
