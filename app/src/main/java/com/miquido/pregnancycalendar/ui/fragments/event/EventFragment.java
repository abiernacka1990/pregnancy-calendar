package com.miquido.pregnancycalendar.ui.fragments.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.miquido.pregnancycalendar.App;
import com.miquido.pregnancycalendar.model.Event;

/**
 * Created by agnieszka on 02.04.16.
 */
public class EventFragment extends Fragment {
    private static final int NO_ID_FOUND_VALUE = -1;
    private static final String ARG_EVENT_ID = "argEventId";
    private int argEventId;
    private Event event;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            argEventId = getArguments().getInt(ARG_EVENT_ID, NO_ID_FOUND_VALUE);
            if (argEventId != NO_ID_FOUND_VALUE) {
                event = App.getInstance().getEventsRepository().get(argEventId);
            }
        }
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
}
