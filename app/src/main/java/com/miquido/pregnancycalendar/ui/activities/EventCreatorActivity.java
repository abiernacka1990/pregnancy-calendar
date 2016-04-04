package com.miquido.pregnancycalendar.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.miquido.pregnancycalendar.App;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.model.Event;
import com.miquido.pregnancycalendar.ui.fragments.event.EventEditFragment;
import com.miquido.pregnancycalendar.ui.fragments.event.EventPreviewFragment;

public class EventCreatorActivity extends AppCompatActivity {

    private static final int NO_ID_FOUND_VALUE = -1;
    public static String ARG_EVENT_ID = "ARG_EVENT_ID";
    private Integer argEventId;
    private Event event;
    private TextView headerTitleTextView;
    private EditText headerTitleEditText;
    private Mode mode;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creator);

        loadBundleArgs();

        initToolbar();
        initView();

        if (mode == null) {
            setMode();
        }

        showFragmentForMode(savedInstanceState, mode);

        updateViewForSpecifiedMode();

    }

    private void setMode() {
        if (checkIntentIfItsNewEvent()) {
            mode = Mode.EDIT;
        } else {
            mode = Mode.PREVIEW;
        }
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

    private void showFragmentForMode(Bundle savedInstanceState, Mode mode) {

        if (savedInstanceState == null) {
            Fragment fragmentToShow = null;
            switch (mode) {
                case EDIT:
                    fragmentToShow = EventEditFragment.newInstance();
                    break;
                case PREVIEW:
                    fragmentToShow = EventPreviewFragment.newInstance(argEventId);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragmentToShow).commit();
        }
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

    private void setEventInfo() {
        headerTitleTextView.setText(event.getTitle());
        headerTitleEditText.setText(event.getTitle());
    }

    private void setViewsVisibility(int editViewsVisibility, int previewViewsVisibility) {
        headerTitleEditText.setVisibility(editViewsVisibility);
        headerTitleTextView.setVisibility(previewViewsVisibility);
        fab.setVisibility(previewViewsVisibility);
    }

    private boolean checkIntentIfItsNewEvent() {
        return event == null;
    }

    private void initView() {
        initFab();
        initHeader();
    }

    private void initHeader() {
        headerTitleTextView = (TextView) findViewById(R.id.textview_title);
        headerTitleEditText = (EditText) findViewById(R.id.edittext_title);
    }

    private void initFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(view -> startEdit());
    }

    private void startEdit() {
        mode = Mode.EDIT;
        updateViewForSpecifiedMode();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, EventEditFragment.newInstance(argEventId))
                .commit();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_delete);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
