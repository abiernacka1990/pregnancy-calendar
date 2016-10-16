package biernacka.pregnancycalendar.adapters.events;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import biernacka.pregnancycalendar.R;
import biernacka.pregnancycalendar.model.Event;

/**
 * Created by agnieszka on 30.03.16.
 */
public abstract class BaseEventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Event> events;
    private Context context;
    private OnItemClickListener onItemClickListener;

    protected void setContext(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return context;
    }

    protected List<Event> getEvents() {
        return events;
    }

    protected void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    protected void setEvents(List<Event> events) {
        this.events = events;
    }

    @NonNull
    protected RecyclerView.ViewHolder getEventViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(v);
    }

    private Event getItem(int position) {
        return events.get(position);
    }

    protected void onBindEventViewHolder(EventViewHolder holder, int position) {
        Event currentItem = getItem(getValidEventPosition(position));
        holder.titleTextView.setText(currentItem.getEventTitleToShow(holder.view.getContext()));
        holder.subtitleTextView.setText(currentItem.getSubtitle(holder.view.getContext()));
        holder.view.setOnClickListener(view -> onItemClickListener.onItemClicked(currentItem));

    }

    protected abstract int getValidEventPosition(int position);

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
