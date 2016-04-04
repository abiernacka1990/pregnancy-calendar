package com.miquido.pregnancycalendar.ui.fragments.event;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miquido.pregnancycalendar.R;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_preview, container, false);
    }

    public static EventPreviewFragment newInstance(int argEventId) {
        EventPreviewFragment fragment = new EventPreviewFragment();
        fragment = (EventPreviewFragment) appendEventIdToArgs(fragment, argEventId);
        return fragment;
    }

}
