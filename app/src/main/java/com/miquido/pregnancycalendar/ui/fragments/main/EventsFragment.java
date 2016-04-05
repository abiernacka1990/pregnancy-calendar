package com.miquido.pregnancycalendar.ui.fragments.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miquido.pregnancycalendar.App;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.adapters.EventsAdapter;
import com.miquido.pregnancycalendar.model.Event;
import com.miquido.pregnancycalendar.ui.activities.EventCreatorActivity;
import com.miquido.pregnancycalendar.ui.decorators.DividerItemDecoration;
import com.miquido.pregnancycalendar.ui.fragments.BaseFragment;

import java.util.Calendar;
import java.util.Date;

public class EventsFragment extends BaseFragment implements EventsAdapter.OnItemClickListener {

    public static final String ARG_SELECTED_DATE = "ARG_SELECTED_DATE";

    private RecyclerView recyclerView;

    private EventsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private long selectedDate;

    public EventsFragment() {
        // Required empty public constructor
    }

    public static EventsFragment newInstance(Date lastSelectedDay) {
        EventsFragment fragment = new EventsFragment();
        if (lastSelectedDay != null) {
            Bundle args = new Bundle();
            args.putLong(ARG_SELECTED_DATE, lastSelectedDay.getTime());
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSelectedDate(savedInstanceState);
    }

    private void setSelectedDate(Bundle savedInstanceState) {
        selectedDate = new Date().getTime();

        if (savedInstanceState != null) {
            selectedDate = savedInstanceState.getLong(ARG_SELECTED_DATE, selectedDate);
        } else if (getArguments() != null) {
            selectedDate = getArguments().getLong(ARG_SELECTED_DATE, selectedDate);
        }
        if (savedInstanceState != null) {
            selectedDate = savedInstanceState.getLong(ARG_SELECTED_DATE, selectedDate);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_events, container, false);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext().getResources());
        recyclerView.addItemDecoration(itemDecoration);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventsAdapter(getContext(), selectedDate, this);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    @Override
    public void onItemClicked(Event event) {
        Intent startEventCreatorActIntent = new Intent(getContext(), EventCreatorActivity.class);
        startEventCreatorActIntent.putExtra(EventCreatorActivity.ARG_EVENT_ID, event.getId());
        startActivity(startEventCreatorActIntent);
    }

    public void updateEventList(Date selectedDate) {
        this.selectedDate = selectedDate.getTime();
        adapter.setDateAnfFindEvents(selectedDate.getTime());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ARG_SELECTED_DATE, selectedDate);
    }
}