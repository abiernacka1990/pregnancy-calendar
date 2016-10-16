package biernacka.pregnancycalendar.adapters.events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ViewGroup;

import java.util.List;

import biernacka.pregnancycalendar.R;
import biernacka.pregnancycalendar.model.Event;

/**
 * Created by agnieszka on 06.07.16.
 */
public class UpcomingEventsAdapter extends BaseEventsAdapter {

    public UpcomingEventsAdapter(Context context, List<Event> events, OnItemClickListener onItemClickListener) {
        setContext(context);
        setEvents(events);
        setOnItemClickListener(onItemClickListener);
    }

    @Override
    protected int getValidEventPosition(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return getEventViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindEventViewHolder((EventViewHolder) holder, position);
    }


    protected void onBindEventViewHolder(EventViewHolder holder, int position) {
        super.onBindEventViewHolder(holder, position);
        float titleTextSize = getContext().getResources().getDimension(R.dimen.dash_font_small);
        holder.titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
    }

    @Override
    public int getItemCount() {
        return getEvents().size();
    }
}
