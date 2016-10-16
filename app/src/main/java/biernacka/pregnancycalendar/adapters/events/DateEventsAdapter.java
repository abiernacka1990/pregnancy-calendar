package biernacka.pregnancycalendar.adapters.events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import biernacka.pregnancycalendar.App;
import biernacka.pregnancycalendar.R;
import biernacka.pregnancycalendar.model.Event;
import biernacka.pregnancycalendar.ui.helpers.PregnancyDatesHelper;
import biernacka.pregnancycalendar.utils.StringFormatter;

/**
 * Created by agnieszka on 06.07.16.
 */
public class DateEventsAdapter extends BaseEventsAdapter {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private long date;

    public DateEventsAdapter(Context context, long date, OnItemClickListener onItemClickListener) {
        setContext(context);
        setDateAndFindEvents(date);
        setOnItemClickListener(onItemClickListener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_event, parent, false);
            return new HeaderViewHolder(v);
        } else if (viewType == TYPE_ITEM) {
            return getEventViewHolder(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            onBindHeaderViewHolder((HeaderViewHolder) holder);
        } else if (holder instanceof EventViewHolder) {
            onBindEventViewHolder((EventViewHolder) holder, position);
        }
    }

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
        return getEvents() == null ? 1 : (getEvents().size() + 1);
    }

    public void setDateAndFindEvents(long date) {
        this.date = date;
        setEvents(App.getInstance().getEventsRepository().getAllEventsForSpecifiedDay(date));
        notifyDataSetChanged();
    }

    private void onBindHeaderViewHolder(HeaderViewHolder holder) {
        HeaderViewHolder headerHolder = holder;
        String headerText = getHeaderText();
        headerHolder.headerTextView.setText(headerText);
    }

    private String getHeaderText() {
        String headerText;
        String dateFormatted = StringFormatter.eventHeaderFormat(date);

        if (PregnancyDatesHelper.isInPregnancyTime(date)) {
            int week = PregnancyDatesHelper.getWeek(date);
            headerText = String.format(getContext().getString(R.string.eventlist_header_with_week), dateFormatted, week);
        } else {
            headerText =  dateFormatted;
        }
        return headerText;
    }

    public Event itemDissmissed(int position) {
        int validElementPosition = getValidEventPosition(position);
        Event deletedItem = getEvents().get(validElementPosition);
        getEvents().remove(validElementPosition);
        notifyItemRemoved(position);
        return deletedItem;
    }

    @Override
    protected int getValidEventPosition(int position) {
        return position - 1;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.headerTextView = (TextView) itemView.findViewById(R.id.textview_header);
        }
    }
}
