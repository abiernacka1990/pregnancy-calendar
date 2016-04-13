package com.miquido.pregnancycalendar.ui.fragments.main.dashboard;

import com.miquido.pregnancycalendar.utils.Preferences;

/**
 * Created by agnieszka on 13.04.16.
 */
public class DashboardFragmentFactory {

    public static BaseDashboardFragment newInstance() {
        if (Preferences.getInstance().isPregnancyStartDateSet()) {
            return new DashboardFragment();
        } else {
            return new EmptyDashboardFragment();
        }
    }
}
