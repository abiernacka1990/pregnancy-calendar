package com.miquido.pregnancycalendar.ui.fragments.dialog;


import android.os.Bundle;
import android.app.Fragment;
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
import com.miquido.pregnancycalendar.db.ormlite.WeightInfoRepository;
import com.miquido.pregnancycalendar.model.WeightInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog to add new weight information.
 * Use the {@link NewWeightInfoDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewWeightInfoDialogFragment extends DialogFragment {

    private EditText editTextWeight;
    private Spinner spinnerWeek;
    private NewWeightInfoListener listener;

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment NewWeightInfoDialogFragment.
     */
    public static NewWeightInfoDialogFragment newInstance(NewWeightInfoListener listener) {
        NewWeightInfoDialogFragment fragment = new NewWeightInfoDialogFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public NewWeightInfoDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_dialog_new_weight_info, container, false);
        editTextWeight = (EditText) mainView.findViewById(R.id.editTextWeight);
        spinnerWeek = (Spinner) mainView.findViewById(R.id.spinnerWeek);
        List<Integer> weeks = new ArrayList<>();
        for(int i = 0; i <=42; i++) {
            weeks.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(),  android.R.layout.simple_spinner_item, weeks);
        spinnerWeek.setAdapter(adapter);
        Button buttonSave = (Button) mainView.findViewById(R.id.buttonSave);
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
        WeightInfo weightInfo = new WeightInfo.Builder()
                .setWeight(weight)
                .setWeek(week)
                .build();

        App.getInstance().getWeightInfoRepository().create(weightInfo);

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
