package com.miquido.pregnancycalendar.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.miquido.pregnancycalendar.App;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.adapters.WeightsAdapter;
import com.miquido.pregnancycalendar.model.Weight;
import com.miquido.pregnancycalendar.ui.fragments.dialog.NewWeightDialogFragment;
import com.miquido.pregnancycalendar.ui.helpers.SimpleItemTouchHelperCallback;
import com.miquido.pregnancycalendar.utils.UsefulUIMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment with weight information and graph
 * Use the {@link WeightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeightFragment extends BaseFragment implements NewWeightDialogFragment.NewWeightListener, com.miquido.pregnancycalendar.adapters.ItemTouchHelper {

    private static final String DIALOG_NEW_WEIGHT = "DIALOG_NEW_WEIGHT";

    private LineChart chart;
    private RecyclerView recyclerView;
    private TextView titleTextView;
    private TextView noDataTextView;

    private WeightsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_weight, container, false);

        titleTextView = (TextView) mainView.findViewById(R.id.text_weight_title);
        noDataTextView = (TextView) mainView.findViewById(R.id.text_weight_no_data);

        recyclerView = (RecyclerView) mainView.findViewById(R.id.recycler_view_weights_list);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WeightsAdapter();
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(this);
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);

        chart = (LineChart) mainView.findViewById(R.id.chart);
        setChartStyle();

        initializeListView();
        refreshData();

        return mainView;
    }

    private void setChartStyle() {
        chart.setDescription("");
        chart.getLegend().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setAxisLineColor(Color.BLACK);
        chart.getAxisLeft().setAxisLineColor(Color.BLACK);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
    }

    private void initializeListView() {
        //TODO

    }

    /**
     * Add new weight info
     */
    private void addWeightInfo() {
        NewWeightDialogFragment.newInstance(this).show(getActivity().getSupportFragmentManager(), DIALOG_NEW_WEIGHT);
    }

    private void refreshData() {

        List<Weight> weightList = App.getInstance().getWeightRepository().getAll();

        boolean emptyList = weightList == null || weightList.isEmpty();
        updateVisibilityOfViews(emptyList);

        if (emptyList) {
            return;
        }

        updateChart(weightList);

        adapter.updateList(weightList);
    }

    private void updateVisibilityOfViews(boolean emptyList) {
        int visibilityOfViewsForData = emptyList ? View.GONE : View.VISIBLE;
        int visibilityOfViewsForNoData = emptyList ? View.VISIBLE : View.GONE;

        chart.setVisibility(visibilityOfViewsForData);
        titleTextView.setVisibility(visibilityOfViewsForData);
        noDataTextView.setVisibility(visibilityOfViewsForNoData);
    }

    private void updateChart(List<Weight> weightList) {

        chart.clear();

        ArrayList<String> xValsAsString = new ArrayList<>();
        ArrayList<Integer> xVals = new ArrayList<>();
        ArrayList<Entry> yVals = new ArrayList<>();

        for (Weight weight : weightList) {
            xVals.add(weight.getWeek());
            yVals.add(new Entry((float) weight.getWeight(), weight.getWeek()));
        }

        for (int i = Collections.min(xVals); i <= Collections.max(xVals); i++) {
            xValsAsString.add(String.valueOf(i));
        }

        LineDataSet dataSet = new LineDataSet(yVals, "DataSet 1");

        //style
        dataSet.setColor(UsefulUIMethods.getColor(R.color.colorPrimaryDark, getActivity()));
        dataSet.setCircleColor(UsefulUIMethods.getColor(R.color.colorPrimaryDark, getActivity()));

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData data = new LineData(xValsAsString, dataSets);

        chart.setData(data);
        chart.animateXY(1000, 1000);

    }

    @Override
    public void onNewWeightsUpdated(Weight weight) {
        refreshData();
    }

    @Override
    public void onItemDismiss(int position) {
        Weight deletedItem = adapter.itemDissmissed(position);
        App.getInstance().getWeightRepository().delete(deletedItem);
       // refreshData();
        Snackbar.make(getView(), R.string.weight_snackbar_item_removed, Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.weight_snackbar_click_to_cancel), v -> {
                    App.getInstance().getWeightRepository().create(deletedItem);
                    refreshData();;
                })
                .show();
    }

    @Override
    public void onBottomFloatingBtClick() {
        addWeightInfo();
    }
}
