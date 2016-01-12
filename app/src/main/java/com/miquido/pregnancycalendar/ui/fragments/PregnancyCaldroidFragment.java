package com.miquido.pregnancycalendar.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miquido.pregnancycalendar.App;
import com.miquido.pregnancycalendar.adapters.PregnancyCaldroidAdapter;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

public class PregnancyCaldroidFragment extends CaldroidFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		return view;
	}

	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		CaldroidGridAdapter adapter = new PregnancyCaldroidAdapter(getActivity(), month, year,
				getCaldroidData(), extraData);
		adapter.setSelectedDates(App.getInstance().getEventsRepository().getAllDates());
		return adapter;
	}

}
