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
public class WeightsAdapter extends BaseRecycleViewAdapter<Weight, WeightsAdapter.ViewHolder> {

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_weight;
    }

    protected ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void updateViewHolder(WeightsAdapter.ViewHolder holder, Weight weight) {
        String weightInfoText = holder.weightInfoTextView.getContext().getString(R.string.item_weight_text);
        String formattedWeightInfoText = String.format(weightInfoText, weight.getWeek(), weight.getWeight(), Preferences.getInstance().getWeightUnit());
        holder.weightInfoTextView.setText(formattedWeightInfoText);
    }

    @Override
    protected List<Weight> getPreparedListOfItems(List<Weight> weights) {
        Collections.sort(weights, (lhs, rhs) -> lhs.getWeek() - rhs.getWeek());
        return weights;
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).getId();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView weightInfoTextView;

        public ViewHolder(View view) {
            super(view);
            weightInfoTextView = (TextView) view.findViewById(R.id.textview_weight_info);
        }
    }
}