package biernacka.pregnancycalendar.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agnieszka on 14.04.16.
 */
public abstract class BaseRecycleViewAdapter<T, R extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<R> {

    private List<T> items = new ArrayList<>();

    protected abstract R getViewHolder(View view);

    protected abstract int getItemLayoutResId();

    protected abstract void updateViewHolder(R holder, T item);

    @Override
    public R onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutResId(), parent, false);
        return getViewHolder(view);
    }

    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public void onBindViewHolder(R holder, int position) {
        T item = getItem(position);
        updateViewHolder(holder, item);
    }

    public void updateList(List<T> items) {
        this.items.clear();
        if (items != null) {
            List<T> itemsPrepared = getPreparedListOfItems(items);
            this.items.addAll(itemsPrepared);
        }
        notifyDataSetChanged();
    }

    protected List<T> getPreparedListOfItems(List<T> incomingItems) {
        return incomingItems;
    }

    public T itemDissmissed(int position) {
        T deletedItem = getItem(position);
        items.remove(position);
        notifyItemRemoved(position);
        return deletedItem;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
