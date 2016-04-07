package com.miquido.pregnancycalendar.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miquido.pregnancycalendar.App;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.model.Event;
import com.miquido.pregnancycalendar.ui.helpers.PregnancyDatesHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by agnieszka on 30.03.16.
 */
public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private static final SimpleDateFormat headerDateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private List<Event> events;
    private Context context;
    private long date;

    private OnItemClickListener onItemClickListener;

    public EventsAdapter(Context context, long date, OnItemClickListener onItemClickListener) {
        this.context = context;
        setDateAnfFindEvents(date);
        this.onItemClickListener = onItemClickListener;
    }

    public void setDateAnfFindEvents(long date) {
        this.date = date;
        events = App.getInstance().getEventsRepository().getAllEventsForSpecifiedDay(date);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_event, parent, false);
            return new HeaderViewHolder(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
            return new EventViewHolder(v);
        }
        return null;
    }

    private Event getItem(int position) {
        return events.get(position);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            onBindHeaderViewHolder((HeaderViewHolder) holder);
        } else if (holder instanceof EventViewHolder) {
            onBindEventViewHolder((EventViewHolder) holder, position);
        }
    }

    private void onBindEventViewHolder(EventViewHolder holder, int position) {
        Event currentItem = getItem(position - 1);
        holder.titleTextView.setText(currentItem.getEventTitle(holder.view.getContext()));
        holder.subtitleTextView.setText(currentItem.getSubtitle(holder.view.getContext()));
        holder.view.setOnClickListener(view -> onItemClickListener.onItemClicked(currentItem));

    }

    private void onBindHeaderViewHolder(HeaderViewHolder holder) {
        HeaderViewHolder headerHolder = holder;
        String headerText = getHeaderText();
        headerHolder.headerTextView.setText(headerText);
    }

    private String getHeaderText() {
        String headerText;
        String dateFormatted = headerDateFormatter.format(new Date(date));

        if (PregnancyDatesHelper.isInPregnancyTime(date)) {
            int week = PregnancyDatesHelper.getWeek(date);
            headerText = String.format(context.getString(R.string.eventlist_header_with_week), dateFormatted, week);
        } else {
            headerText = String.format(context.getString(R.string.eventlist_header_without_week), dateFormatted);
        }
        return headerText;
    }

    //    need to override this method
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return events == null ? 1 : (events.size() + 1);
    }

    public Event itemDissmissed(int position) {
        int positionWithoutHeader = position - 1;
        Event deletedItem = events.get(positionWithoutHeader);
        events.remove(positionWithoutHeader);
        notifyItemRemoved(position);
        return deletedItem;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.headerTextView = (TextView) itemView.findViewById(R.id.textview_header);
        }
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView titleTextView;
        TextView subtitleTextView;

        public EventViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.titleTextView = (TextView) itemView.findViewById(R.id.textview_title);
            this.subtitleTextView = (TextView) itemView.findViewById(R.id.textview_subtitle);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Event event);
    }
}
