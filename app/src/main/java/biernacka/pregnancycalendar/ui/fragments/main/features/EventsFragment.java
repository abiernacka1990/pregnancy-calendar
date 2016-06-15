package biernacka.pregnancycalendar.ui.fragments.main.features;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import biernacka.pregnancycalendar.App;
import biernacka.pregnancycalendar.R;
import biernacka.pregnancycalendar.adapters.EventsAdapter;
import biernacka.pregnancycalendar.model.Event;
import biernacka.pregnancycalendar.ui.activities.EventCreatorActivity;
import biernacka.pregnancycalendar.ui.activities.MainActivity;
import biernacka.pregnancycalendar.ui.decorators.DividerItemDecoration;
import biernacka.pregnancycalendar.ui.fragments.main.MainFragment;

import java.util.Date;

public class EventsFragment extends MainFragment implements EventsAdapter.OnItemClickListener, biernacka.pregnancycalendar.adapters.ItemTouchHelper {

    public static final String ARG_SELECTED_DATE = "ARG_SELECTED_DATE";

    private RecyclerView recyclerView;

    private EventsAdapter adapter;
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
        initRecycleView();
        return recyclerView;
    }

    private void initRecycleView() {
        initRecycleViewStyle();
        setAdapter();
        setSwipe2DismissEnabled(recyclerView, this);
    }

    private void setAdapter() {
        adapter = new EventsAdapter(getContext(), selectedDate, this);
        recyclerView.setAdapter(adapter);
    }

    private void initRecycleViewStyle() {
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext().getResources());
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onItemClicked(Event event) {
        Intent startEventCreatorActIntent = new Intent(getContext(), EventCreatorActivity.class);
        startEventCreatorActIntent.putExtra(EventCreatorActivity.ARG_EVENT_ID, event.getId());
        getActivity().startActivityForResult(startEventCreatorActIntent, MainActivity.EVENTS_CHANGED_REQUEST);
    }

    public void updateEventList(Date selectedDate) {
        this.selectedDate = selectedDate.getTime();
        updateEventList();
    }

    private void updateEventList() {
        adapter.setDateAnfFindEvents(selectedDate);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ARG_SELECTED_DATE, selectedDate);
    }

    @Override
    public void onItemDismiss(int position) {
        Event deletedItem = adapter.itemDissmissed(position);
        App.getInstance().getEventsRepository().delete(deletedItem);
        showOnItemDismissSnackbar(deletedItem);
        updateEventList();
    }

    private void showOnItemDismissSnackbar(Event deletedItem) {
        Snackbar.make(getView(), R.string.event_snackbar_item_removed, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.event_snackbar_click_to_cancel), v -> {
                    App.getInstance().getEventsRepository().create(deletedItem);
                    updateEventList();
                })
                .show();
    }

    @Override
    public boolean isExpandedAppBarEnabled() {
        return true;
    }

    @Override
    public boolean isFabBottomVisible() {
        return false;
    }
}
