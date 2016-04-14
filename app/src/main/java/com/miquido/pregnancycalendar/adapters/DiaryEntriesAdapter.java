package com.miquido.pregnancycalendar.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.model.DiaryEntry;
import com.miquido.pregnancycalendar.utils.StringFormatter;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

/**
 * Created by agnieszka on 13.04.16.
 */
public class DiaryEntriesAdapter extends BaseRecycleViewAdapter<DiaryEntry, DiaryEntriesAdapter.ViewHolder> {

    @Override
    protected ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected int getItemLayoutResId() {
        return R.layout.item_diary_entry;
    }

    @Override
    protected void updateViewHolder(ViewHolder holder, DiaryEntry item) {
        holder.diaryEntryTextTextView.setText(item.getText());
        holder.diaryEntryDateTextView.setText(StringFormatter.withDayOfWeekDate(new DateTime(item.getDate())));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView diaryEntryTextTextView;
        TextView diaryEntryDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            diaryEntryTextTextView = (TextView) itemView.findViewById(R.id.texview_diary_entry_text);
            diaryEntryDateTextView = (TextView) itemView.findViewById(R.id.texview_diary_entry_date);
        }
    }
}
