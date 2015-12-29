package com.miquido.pregnancycalendar.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.miquido.pregnancycalendar.App;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.model.WeightInfo;
import com.miquido.pregnancycalendar.ui.fragments.dialog.NewWeightInfoDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment with weight information and graph
 * Use the {@link WeightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeightFragment extends BaseFragment implements NewWeightInfoDialogFragment.NewWeightInfoListener {

    private static final String DIALOG_NEW_WEIGHT = "DIALOG_NEW_WEIGHT";
    private GraphView graph;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WeightFragment.
     */
    public static WeightFragment newInstance() {
        return new WeightFragment();
    }

    public WeightFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_weight, container, false);

        graph = (GraphView) mainView.findViewById(R.id.graph);
        FloatingActionButton fabAddWeight = (FloatingActionButton) mainView.findViewById(R.id.fabAddWeight);
        fabAddWeight.setOnClickListener(view -> addWeightInfo());

        initializeListView();
        refreshData();
        return mainView;
    }

    private void initializeListView() {
        //TODO

    }

    /** Add new weight info
     *
     */
    private void addWeightInfo() {
        NewWeightInfoDialogFragment.newInstance(this).show(getFragmentManager(), DIALOG_NEW_WEIGHT);
    }

    private void refreshData() {

        List<WeightInfo> data = App.getInstance().getWeightInfoRepository().getAll();

        List<DataPoint> dataPointList = new ArrayList<>();
        for(WeightInfo weightInfo: data) {
            dataPointList.add(new DataPoint(weightInfo.getWeek(), weightInfo.getWeight()));
        }
        Collections.sort(dataPointList, (lhs, rhs) -> (int) (lhs.getX() - rhs.getX()));

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPointList.toArray(new DataPoint[dataPointList.size()]));
        series.setColor(getResources().getColor(R.color.colorPrimaryDark));
        graph.removeAllSeries();
        graph.addSeries(series);
        //TODO: refresh adapter
    }

    @Override
    public void onNewWeightInfoAdded(int week, double weight) {
        refreshData();
    }



}
