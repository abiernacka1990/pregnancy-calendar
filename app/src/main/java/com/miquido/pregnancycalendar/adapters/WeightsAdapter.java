package com.miquido.pregnancycalendar.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.model.Weight;
import com.miquido.pregnancycalendar.utils.Preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by agnieszka on 08.01.16.
 */
public class WeightsAdapter extends RecyclerView.Adapter<WeightsAdapter.ViewHolder> {

    private List<Weight> weights = new ArrayList<>();

    public WeightsAdapter() {
    }

    @Override
    public WeightsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weight, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Weight weight = weights.get(position);
        String weightInfoText = holder.weightInfoTextView.getContext().getString(R.string.item_weight_text);
        String formattedWeightInfoText = String.format(weightInfoText, weight.getWeek(), weight.getWeight(), Preferences.getInstance().getWeightUnit());
        holder.weightInfoTextView.setText(formattedWeightInfoText);
    }

    public void updateList(List<Weight> weights) {
        this.weights.clear();
        this.weights.addAll(weights);
        Collections.sort(this.weights, (lhs, rhs) -> lhs.getWeek() - rhs.getWeek());
        notifyDataSetChanged();
    }

    public Weight itemDissmissed(int position) {
        Weight deletedItem = weights.get(position);
        weights.remove(position);
        notifyItemRemoved(position);
        return deletedItem;
    }

    @Override
    public int getItemCount() {
        return weights.size();
    }

    @Override
    public long getItemId(int i) {
        return weights.get(i).getId();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView weightInfoTextView;

        public ViewHolder(View view) {
            super(view);
            weightInfoTextView = (TextView) view.findViewById(R.id.textview_weight_info);
        }
    }
}