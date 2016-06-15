package biernacka.pregnancycalendar.ui.fragments.main.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import biernacka.pregnancycalendar.R;
import biernacka.pregnancycalendar.ui.fragments.main.MainFragment;
import biernacka.pregnancycalendar.utils.Preferences;
import biernacka.pregnancycalendar.utils.StringFormatter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * Fragment with user settings.
 */
public class SettingsFragment extends MainFragment implements DatePickerDialog.OnDateSetListener {

    private TextView dataTextView;
    private EditText weightUnitEditText;

    private OnSettingsChangesListener settingsChangesListener;
    private ImageView plannedBirthDateImageView;

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
        dataTextView = (TextView) mainView.findViewById(R.id.textview_start_date);
        plannedBirthDateImageView = (ImageView) mainView.findViewById(R.id.image_view_start_date);
        weightUnitEditText = (EditText) mainView.findViewById(R.id.textview_weight_unit);
        showPreferences();
        plannedBirthDateImageView.setOnClickListener(view -> openDateChooser());
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
        long plannedBirthDateAsLong = Preferences.getInstance().getPlannedBirthDate();
        if (plannedBirthDateAsLong != 0) {
            dateToPick.setTimeInMillis(plannedBirthDateAsLong);
        }
        return dateToPick;
    }

    public void onPlannedBirthDateChanged() {
        if (settingsChangesListener != null) {
            //TODO:
            settingsChangesListener.onPlannedBirthDateChanged();
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
        savePlannedBirthDate(selectedDateInMillis);
        onPlannedBirthDateChanged();
    }

    private long getSelectedDateInMillis(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
        return calendar.getTimeInMillis();
    }

    @Override
    public boolean isExpandedAppBarEnabled() {
        return false;
    }

    @Override
    public boolean isFabBottomVisible() {
        return false;
    }

    public interface OnSettingsChangesListener {
        void onPlannedBirthDateChanged();
    }

    private void savePlannedBirthDate(long plannedBirthDate) {
        Preferences.getInstance().setPlannedBirthDate(plannedBirthDate);
        showPreferences();
    }

    private void saveWeightUnit() {
        Preferences.getInstance().setWeightUnit(weightUnitEditText.getEditableText().toString());
    }

    private void showPreferences() {
        long date = Preferences.getInstance().getPlannedBirthDate();
        String dateInfo = date == 0 ? getString(R.string.unknown_date) : StringFormatter.simpleDate(date);
        dataTextView.setText(dateInfo);
        String defaultWeightUnit = Preferences.getInstance().getWeightUnit();
        weightUnitEditText.setText(defaultWeightUnit);
        weightUnitEditText.setSelection(weightUnitEditText.getText().length());
    }

}