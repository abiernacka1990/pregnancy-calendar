package com.miquido.pregnancycalendar.ui.fragments.dialog;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.miquido.pregnancycalendar.App;
import com.miquido.pregnancycalendar.BuildConfig;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.model.Weight;

import java.util.ArrayList;
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
        // Inflate the layout for this fragment
        getDialog().setTitle(R.string.dialog_new_weight_title);
        View mainView = inflater.inflate(R.layout.dialog_new_weight, container, false);
        TextView weightLabelTextView = (TextView) mainView.findViewById(R.id.text_weight_label);
        weightLabelTextView.setText(String.format(getString(R.string.dialog_new_weight_weight), App.getInstance().getDefaultWeightUnit()));
        editTextWeight = (EditText) mainView.findViewById(R.id.edittext_weight);
        spinnerWeek = (Spinner) mainView.findViewById(R.id.spinner_week);
        List<Integer> weeks = new ArrayList<>();
        for(int i = 0; i <=42; i++) {
            weeks.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(),  android.R.layout.simple_spinner_item, weeks);
        spinnerWeek.setAdapter(adapter);
        Button buttonSave = (Button) mainView.findViewById(R.id.button_save);
        buttonSave.setOnClickListener(view -> {
            addNewWeightInfo();
        });
        return mainView;
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

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Size of weight before inserting: " + App.getInstance().getWeightRepository().getAll().size());
        }
        App.getInstance().getWeightRepository().create(weightInfo);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Size of weight after inserting: " + App.getInstance().getWeightRepository().getAll().size());
        }

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
            .setNegativeButton(R.string.cancel, (dialog, id) -> {})
            .create().show();
    }

    private void updateData(Weight weight) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Size of weight before updating: " + App.getInstance().getWeightRepository().getAll().size());
        }
        App.getInstance().getWeightRepository().updateSpecifiedWeek(weight);
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Size of weight after updating: " + App.getInstance().getWeightRepository().getAll().size());
        }

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
