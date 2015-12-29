package com.miquido.pregnancycalendar.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.utils.Preferences;
import com.miquido.pregnancycalendar.utils.StringFormatter;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Date;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Fragment with user settings.
 */
public class SettingsFragment extends BaseFragment {

    private final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
    private final String dialogSavedStateKey = "DIALOG_CALDROID_SAVED_STATE";

    private TextView dataTextView;

    private OnSettingsChangesListener settingsChangesListener;
    private CaldroidFragment dateDialogFragment;

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    public SettingsFragment() {
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
        View mainView = inflater.inflate(R.layout.fragment_settings, container, false);
        dataTextView = (TextView) mainView.findViewById(R.id.text_start_date);
        updatePregnancyStartDateInfo();
        dataTextView.setOnClickListener(view -> openDateChooser());

        if(savedInstanceState != null) {
            dateDialogFragment = new CaldroidFragment();
            dateDialogFragment.restoreDialogStatesFromKey(
                    getAppCompatActivity().getSupportFragmentManager(), savedInstanceState,
                    dialogSavedStateKey, dialogTag);
            Bundle args = dateDialogFragment.getArguments();
            if (args == null) {
                args = new Bundle();
                dateDialogFragment.setArguments(args);
            }
        }

        return mainView;
    }

    /**
     * Open Dialog to choose a date (start of pregnancy). Set selected date.
     */
    private void openDateChooser() {

        DateTime dateToSelect = getDateToSelect();

        dateDialogFragment = new CaldroidFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CaldroidFragment.YEAR, dateToSelect.getYear());
        bundle.putInt(CaldroidFragment.MONTH, dateToSelect.getMonth());
        dateDialogFragment.setArguments(bundle);
        dateDialogFragment.setCalendarDateTime(dateToSelect);
        dateDialogFragment.setSelectedDate(new Date(dateToSelect.getMilliseconds(TimeZone.getDefault())));

        dateDialogFragment.setCaldroidListener(dateDialogListener);
        dateDialogFragment.show(getAppCompatActivity().getSupportFragmentManager(), dialogTag);
    }

    @NonNull
    private DateTime getDateToSelect() {
        long pregnancyStartDateAsLong = Preferences.getInstance().getPregnancyStartDate();
        return pregnancyStartDateAsLong == 0 ?
                DateTime.now(TimeZone.getDefault()) : DateTime.forInstant(pregnancyStartDateAsLong, TimeZone.getDefault());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onPregnancyStartDateChanged() {
        if (settingsChangesListener != null) {
            //TODO:
            settingsChangesListener.onPregnancyStartDateChanged(0);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            settingsChangesListener = (OnSettingsChangesListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSettingsChangesListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        settingsChangesListener = null;
    }

    public interface OnSettingsChangesListener {
        void onPregnancyStartDateChanged(long date);
    }

    final CaldroidListener dateDialogListener = new CaldroidListener() {
        @Override
        public void onSelectDate(Date date, View view) {
            savePregnancyStartDate(date);
            dateDialogFragment.dismiss();

        }

    };

    private void savePregnancyStartDate(Date date) {
        Preferences.getInstance().setPregnancyStartDate(date.getTime());
        updatePregnancyStartDateInfo();
    }

    private void updatePregnancyStartDateInfo() {
        long date = Preferences.getInstance().getPregnancyStartDate();
        String dateInfo = date == 0 ? getString(R.string.unknown_date) : StringFormatter.date(date);
        dataTextView.setText(dateInfo);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (dateDialogFragment != null) {
            dateDialogFragment.saveStatesToKey(outState, dialogSavedStateKey);
        }
    }

}
