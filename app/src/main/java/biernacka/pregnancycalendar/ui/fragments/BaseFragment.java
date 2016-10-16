package biernacka.pregnancycalendar.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import biernacka.pregnancycalendar.ui.helpers.SimpleItemTouchHelperCallback;

/**
 * Created by agnieszka on 03.11.15.
 *
 * Base fragment class for application.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void onBottomFloatingBtClick() {

    }

    protected void setSwipe2DismissEnabled(RecyclerView recyclerView, biernacka.pregnancycalendar.adapters.helper.ItemTouchHelper itemTouchHelper) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(itemTouchHelper);
        new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
    }

}
