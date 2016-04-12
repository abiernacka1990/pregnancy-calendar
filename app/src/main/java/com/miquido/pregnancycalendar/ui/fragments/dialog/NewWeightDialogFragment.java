package com.miquido.pregnancycalendar.ui.fragments.dialog;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.miquido.pregnancycalendar.App;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.model.Weight;
import com.miquido.pregnancycalendar.ui.helpers.PregnancyDatesHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Dialog to add new weight information.
 * Use the {@link NewWeightDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewWeightDialogFragment extends DialogFragment {

    private static final String TAG = "NewWeightDialogF tag";
    private EditText editTextWeight;
    private Spinner spinnerWeek;
    private NewWeightListener listener;
    private TextView weightLabelTextView;
    private Button buttonSave;

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment NewWeightDialogFragment.
     */
    public static NewWeightDialogFragment newInstance(NewWeightListener listener) {
        NewWeightDialogFragment fragment = new NewWeightDialogFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public NewWeightDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(R.string.dialog_new_weight_title);
        View mainView = inflater.inflate(R.layout.dialog_new_weight, container, false);
        initView(mainView);
        return mainView;
    }

    private void initView(View mainView) {
        findViews(mainView);
        initLabels();
        initWeeksAdapter();
        initSaveButton();
        initWeightTextWatcher();
    }

    private void initLabels() {
        weightLabelTextView.setText(String.format(getString(R.string.dialog_new_weight_weight),
                App.getInstance().getDefaultWeightUnit()));
    }

    private void initSaveButton() {
        buttonSave.setEnabled(false);
        buttonSave.setOnClickListener(view -> {
            addNewWeightInfo();
        });
    }

    private void initWeeksAdapter() {

        List<Integer> weeks = new ArrayList<>();
        for (int i = 0; i <= 42; i++) {
            weeks.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, weeks);
        spinnerWeek.setAdapter(adapter);

        long now = Calendar.getInstance().getTimeInMillis();
        if (PregnancyDatesHelper.isInPregnancyTime(now)) {
            int week = PregnancyDatesHelper.getWeek(now);
            spinnerWeek.setSelection(week);
        }
    }

    private void findViews(View mainView) {
        weightLabelTextView = (TextView) mainView.findViewById(R.id.text_weight_label);
        editTextWeight = (EditText) mainView.findViewById(R.id.edittext_weight);
        spinnerWeek = (Spinner) mainView.findViewById(R.id.spinner_week);
        buttonSave = (Button) mainView.findViewById(R.id.button_save);
    }

    private void initWeightTextWatcher() {
        editTextWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setSaveButtonAvailability();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setSaveButtonAvailability();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setSaveButtonAvailability();
            }
        });
    }

    private void setSaveButtonAvailability() {
        buttonSave.setEnabled(isValid());
    }

    private boolean isValid() {
        try {
            Double.valueOf(editTextWeight.getEditableText().toString());
        } catch (NumberFormatException exc) {
            return false;
        }
        return true;
    }

    private void addNewWeightInfo() {
        int week = (int) spinnerWeek.getSelectedItem();
        double weight = 0;
        try {
            weight = Double.valueOf(editTextWeight.getEditableText().toString());
        } catch (NumberFormatException exc) {
            editTextWeight.setError(getString(R.string.incorrect_value));
            return;
        }
        Weight weightInfo = new Weight.Builder()
                .setWeight(weight)
                .setWeek(week)
                .build();

        if (App.getInstance().getWeightRepository().exist(week)) {
            confirmUpdatingData(weightInfo);
            return;
        }

        App.getInstance().getWeightRepository().create(weightInfo);
        dismiss();
        listener.onNewWeightsUpdated(weightInfo);
    }

    private void confirmUpdatingData(Weight weight) {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.dialog_confirm_updating_data_message)
                .setPositiveButton(R.string.yes, (dialog, id) -> {
                    dismiss();
                    updateData(weight);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                })
                .create().show();
    }

    private void updateData(Weight weight) {
        App.getInstance().getWeightRepository().updateSpecifiedWeek(weight);
        dismiss();
        listener.onNewWeightsUpdated(weight);
    }

    public void setListener(NewWeightListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }

    public interface NewWeightListener {
        void onNewWeightsUpdated(Weight weight);
    }

}
