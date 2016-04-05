package com.miquido.pregnancycalendar.ui.activities.helper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.miquido.pregnancycalendar.BuildConfig;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.ui.activities.MainActivity;
import com.miquido.pregnancycalendar.ui.fragments.main.EventsFragment;
import com.miquido.pregnancycalendar.ui.fragments.main.SettingsFragment;
import com.miquido.pregnancycalendar.ui.fragments.main.WeightFragment;

/**
 * Created by agnieszka on 05.04.16.
 */
public enum MainFragmentSettings {
    EVENTS(EventsFragment.class, Constants.EXPANDED_APPBAR_ENABLED, !Constants.FAB_BOTTOM_VISIBILE),
    WEIGHT(WeightFragment.class, !Constants.EXPANDED_APPBAR_ENABLED, Constants.FAB_BOTTOM_VISIBILE),
    SETTINGS(SettingsFragment.class, !Constants.EXPANDED_APPBAR_ENABLED, !Constants.FAB_BOTTOM_VISIBILE);

    private static String TAG = "MainFragmentSettings";
    private Class<? extends Fragment> fragmentClass;
    private boolean expandedAppBarEnabled;
    private boolean fabBottomVisible;

    MainFragmentSettings(Class<? extends Fragment> fragmentClass, boolean expandedAppBarEnabled, boolean fabBottomVisible) {
        this.fragmentClass = fragmentClass;
        this.expandedAppBarEnabled = expandedAppBarEnabled;
        this.fabBottomVisible = fabBottomVisible;
    }

    @Nullable
    public Fragment getFragment() {
        try {
            return fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setFragmentAndAppBarBehaviour(AppBarLayout appBarLayout, NestedScrollView nestedScrollView) {
        appBarLayout.setExpanded(expandedAppBarEnabled, false);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return expandedAppBarEnabled;
            }
        });
        nestedScrollView.setNestedScrollingEnabled(expandedAppBarEnabled);
    }

    public static MainFragmentSettings getFragmentByNavDrawerItem(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            return SETTINGS;
        } else if (id == R.id.nav_weight) {
            return WEIGHT;
        } else if (id == R.id.nav_calendar) {
            return EVENTS;
        } else {
            logNoItemFound(item);
            return null;
        }
    }

    private static void logNoItemFound(MenuItem item) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "No fragment specified for selected item: " + item.toString());
        }
    }

    public void setFabBottomVisibility(FloatingActionButton fabBottom) {
        if (fabBottomVisible) {
            fabBottom.setVisibility(View.VISIBLE);
        } else {
            fabBottom.setVisibility(View.GONE);
        }
    }

    public static MainFragmentSettings findFragmentByClass(Class<? extends Fragment> aClass) {
        for (MainFragmentSettings mainFragment : MainFragmentSettings.values()) {
            if (mainFragment.fragmentClass == aClass) {
                return mainFragment;
            }
        }
        throw new RuntimeException("No MainFragment found for specified class:" + aClass.toString());
    }

    private static class Constants {
        public static final boolean EXPANDED_APPBAR_ENABLED = true;
        public static final boolean FAB_BOTTOM_VISIBILE = true;
    }
}
