package com.miquido.pregnancycalendar.ui.fragments.dialog;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.miquido.pregnancycalendar.App;
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

    private EditText editTextWeight;
    private Spinner spinnerWeek;
    private NewWeightInfoListener listener;

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment NewWeightDialogFragment.
     */
    public static NewWeightDialogFragment newInstance(NewWeightInfoListener listener) {
        NewWeightDialogFragment fragment = new NewWeightDialogFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public NewWeightDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.dialog_new_weight, container, false);
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

        App.getInstance().getWeightRepository().create(weightInfo);

        dismiss();
        listener.onNewWeightInfoAdded(week, weight);
    }

    public void setListener(NewWeightInfoListener listener) {
        this.listener = listener;
    }

    public interface NewWeightInfoListener {
        void onNewWeightInfoAdded(int week, double weight);
    }

}
