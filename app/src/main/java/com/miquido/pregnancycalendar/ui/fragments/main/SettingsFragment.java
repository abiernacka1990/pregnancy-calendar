package com.miquido.pregnancycalendar.ui.fragments.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.ui.fragments.BaseFragment;
import com.miquido.pregnancycalendar.utils.Preferences;
import com.miquido.pregnancycalendar.utils.StringFormatter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * Fragment with user settings.
 */
public class SettingsFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    private TextView dataTextView;
    private EditText weightUnitEditText;

    private OnSettingsChangesListener settingsChangesListener;
    private ImageView startDateImageView;

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
        startDateImageView = (ImageView) mainView.findViewById(R.id.imageViewStartDate);
        weightUnitEditText = (EditText) mainView.findViewById(R.id.text_weight_unit);
        showPreferences();
        startDateImageView.setOnClickListener(view -> openDateChooser());
        return mainView;
    }

    /**
     * Open Dialog to choose a date (start of pregnancy). Set selected date.
     */
    private void openDateChooser() {
        Calendar dateToSelectByDefault = getDateToSelectByDefault();
        DatePickerDialog startPregnancyDatePickerDialog = DatePickerDialog.newInstance(
                SettingsFragment.this,
                dateToSelectByDefault.get(Calendar.YEAR),
                dateToSelectByDefault.get(Calendar.MONTH),
                dateToSelectByDefault.get(Calendar.DAY_OF_MONTH)
        );
        startPregnancyDatePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    @NonNull
    private Calendar getDateToSelectByDefault() {
        Calendar dateToPick = Calendar.getInstance();
        long pregnancyStartDateAsLong = Preferences.getInstance().getPregnancyStartDate();
        if (pregnancyStartDateAsLong != 0) {
            dateToPick.setTimeInMillis(pregnancyStartDateAsLong);
        }
        return dateToPick;
    }

    public void onPregnancyStartDateChanged() {
        if (settingsChangesListener != null) {
            //TODO:
            settingsChangesListener.onPregnancyStartDateChanged();
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

    @Override
    public void onPause() {
        saveWeightUnit();
        super.onPause();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        view.dismiss();
        long selectedDateInMillis = getSelectedDateInMillis(year, monthOfYear, dayOfMonth);
        savePregnancyStartDate(selectedDateInMillis);
        onPregnancyStartDateChanged();
    }

    private long getSelectedDateInMillis(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
        return calendar.getTimeInMillis();
    }

    public interface OnSettingsChangesListener {
        void onPregnancyStartDateChanged();
    }

    private void savePregnancyStartDate(long startDateInMillis) {
        Preferences.getInstance().setPregnancyStartDate(startDateInMillis);
        showPreferences();
    }

    private void saveWeightUnit() {
        Preferences.getInstance().setWeightUnit(weightUnitEditText.getEditableText().toString());
    }

    private void showPreferences() {
        long date = Preferences.getInstance().getPregnancyStartDate();
        String dateInfo = date == 0 ? getString(R.string.unknown_date) : StringFormatter.date(date);
        dataTextView.setText(dateInfo);
        String defaultWeightUnit = Preferences.getInstance().getWeightUnit();
        weightUnitEditText.setText(defaultWeightUnit);
        weightUnitEditText.setSelection(weightUnitEditText.getText().length());
    }

}
