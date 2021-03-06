package com.miquido.pregnancycalendar.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.utils.Preferences;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.CalendarHelper;

import java.util.HashMap;

import hirondelle.date4j.DateTime;

public class PregnancyCaldroidAdapter extends CaldroidGridAdapter {

    public static final int NUMBER_OF_WEEKS = 40;

    public PregnancyCaldroidAdapter(Context context, int month, int year,
                                    HashMap<String, Object> caldroidData,
                                    HashMap<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;

        if (convertView == null) {
            cellView = inflater.inflate(R.layout.cell_calendar, null);
        }

        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        TextView dateTextView = (TextView) cellView.findViewById(R.id.text_date);
        TextView extraInfoTextView = (TextView) cellView.findViewById(R.id.text_extra_info);
        ImageView pointerImageView = (ImageView) cellView.findViewById(R.id.image_pointer);

        pointerImageView.setVisibility(View.INVISIBLE);

        dateTextView.setTextColor(Color.BLACK);

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();

        // Set color of the dates in previous / next month
        if (dateTime.getMonth() != month) {
            dateTextView.setTextColor(resources.getColor(com.caldroid.R.color.caldroid_darker_gray));
        }

        // Customize for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            pointerImageView.setVisibility(View.VISIBLE);
        }

        if (dateTime.equals(getToday())) {
            cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
        } else {
            cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
        }

        dateTextView.setText(String.valueOf(dateTime.getDay()));
        extraInfoTextView.setText(getWeekInfo(dateTime));

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);


        return cellView;
    }

    private String getWeekInfo(DateTime dateTime) {

        String defaultInfo = "";

        long pregnancyStartDate = Preferences.getInstance().getPregnancyStartDate();

        if (pregnancyStartDate > 0) {
            final long now = CalendarHelper.convertDateTimeToDate(dateTime).getTime();
            int week = (int) ((now - pregnancyStartDate) / (1000 * 60 * 60 * 24 * 7)) + 1;
            if (now >= pregnancyStartDate && week <= NUMBER_OF_WEEKS) {
                return "(" + String.valueOf(week) + ")";
            }
        }

        return defaultInfo;

    }

}
