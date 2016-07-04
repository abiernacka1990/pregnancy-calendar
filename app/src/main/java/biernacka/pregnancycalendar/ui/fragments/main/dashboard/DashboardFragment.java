package biernacka.pregnancycalendar.ui.fragments.main.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import biernacka.pregnancycalendar.R;

/**
 * Created by agnieszka on 13.04.16.
 */
public class DashboardFragment extends BaseDashboardFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initView(mainView);
        return mainView;
    }

    private void initView(View mainView) {
        //TODO
    }
}
