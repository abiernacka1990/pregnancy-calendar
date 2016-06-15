package biernacka.pregnancycalendar.ui.fragments.main.dashboard;

import biernacka.pregnancycalendar.ui.fragments.main.MainFragment;

/**
 * Created by agnieszka on 13.04.16.
 */
public abstract class BaseDashboardFragment extends MainFragment {

    @Override
    public boolean isExpandedAppBarEnabled() {
        return false;
    }

    @Override
    public boolean isFabBottomVisible() {
        return false;
    }
}
