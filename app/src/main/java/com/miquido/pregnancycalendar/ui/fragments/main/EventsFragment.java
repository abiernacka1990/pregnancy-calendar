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

    private RecyclerView recyclerView;

    private EventsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public EventsFragment() {
        // Required empty public constructor
    }

    public static EventsFragment newInstance() {
        return new EventsFragment();
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
        adapter = new EventsAdapter(getContext(), Calendar.getInstance().getTimeInMillis(), this);
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
        adapter.setDateAnfFindEvents(selectedDate.getTime());
        adapter.notifyDataSetChanged();
    }
}
