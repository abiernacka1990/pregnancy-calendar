package com.miquido.pregnancycalendar.ui.fragments.event;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.miquido.pregnancycalendar.App;
import com.miquido.pregnancycalendar.model.Event;
import com.miquido.pregnancycalendar.ui.fragments.BaseFragment;

/**
 * Created by agnieszka on 02.04.16.
 */
public class EventFragment extends BaseFragment {
    private static final int NO_ID_FOUND_VALUE = -1;
    private static final String ARG_EVENT_ID = "argEventId";
    private int argEventId;
    private Event event;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            argEventId = getArguments().getInt(ARG_EVENT_ID, NO_ID_FOUND_VALUE);
            if (argEventId != NO_ID_FOUND_VALUE) {
                event = App.getInstance().getEventsRepository().get(argEventId);
            }
        }
    }

    protected void finishActivityWithResultOk() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    public static <T extends EventFragment> T appendEventIdToArgs(T eventFragment, int argEventId) {
        Bundle args = eventFragment.getArguments();
        if (args == null) {
            args = new Bundle();
        }
        args.putInt(ARG_EVENT_ID, argEventId);
        eventFragment.setArguments(args);
        return eventFragment;
    }

    protected Event getEvent() {
        return event;
    }
}
