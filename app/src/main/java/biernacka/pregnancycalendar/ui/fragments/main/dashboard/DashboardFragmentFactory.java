package biernacka.pregnancycalendar.ui.fragments.main.dashboard;

import biernacka.pregnancycalendar.utils.Preferences;

/**
 * Created by agnieszka on 13.04.16.
 */
public class DashboardFragmentFactory {

    public static BaseDashboardFragment newInstance() {
        if (Preferences.getInstance().isPlannedBirthDateSet()) {
            return new DashboardFragment();
        } else {
            return new EmptyDashboardFragment();
        }
    }
}
