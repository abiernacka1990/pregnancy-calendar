package biernacka.pregnancycalendar.ui.fragments.main.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import biernacka.pregnancycalendar.R;
import biernacka.pregnancycalendar.ui.activities.MainActivity;

/**
 * Created by agnieszka on 13.04.16.
 */
public class EmptyDashboardFragment extends BaseDashboardFragment {

    public EmptyDashboardFragment() {
        //required
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_dashboard_empty, container, false);
        initView(mainView);
        return mainView;
    }

    private void initView(View mainView) {
        mainView.findViewById(R.id.imageview_dashboard_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).selectSettingsFragment();
            }
        });
    }
}
