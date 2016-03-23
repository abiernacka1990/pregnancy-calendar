package com.miquido.pregnancycalendar.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.miquido.pregnancycalendar.BuildConfig;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.ui.decorators.PregnancyDayDecorator;
import com.miquido.pregnancycalendar.ui.fragments.BaseFragment;
import com.miquido.pregnancycalendar.ui.fragments.EventsFragment;
import com.miquido.pregnancycalendar.ui.fragments.SettingsFragment;
import com.miquido.pregnancycalendar.ui.fragments.WeightFragment;
import com.samsistemas.calendarview.decor.DayDecorator;
import com.samsistemas.calendarview.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.OnSettingsChangesListener {

    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout toolbarLayout;
    private CalendarView calendarView;
    private NestedScrollView nestedScrollViewMain;
    private Fragment currentFragment;
    private FloatingActionButton fabBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        initToolbar();
        initToogle();
        initNavDrawer();
        initContent();
        showFragment(savedInstanceState);
        initCalendar();
    }

    private void initContent() {
        nestedScrollViewMain = (NestedScrollView) findViewById(R.id.nested_scrollview_main);
        fabBottom = (FloatingActionButton) findViewById(R.id.fab_bottom);
        fabBottom.setOnClickListener(view -> sendOnBottmFabClickEventToFragment());
    }

    private void sendOnBottmFabClickEventToFragment() {
        if(currentFragment instanceof BaseFragment) {
            ((BaseFragment) currentFragment).onBottomFloatingBtClick();
        }
    }

    private void initCalendar() {

        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        calendarView.setDecoratorsList(new ArrayList<DayDecorator>(Arrays.asList(new PregnancyDayDecorator())));
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        calendarView.setIsOverflowDateVisible(true);
        calendarView.setCurrentDay(new Date(System.currentTimeMillis()));
        calendarView.refreshCalendar(Calendar.getInstance(Locale.getDefault()));
        calendarView.setOnDateClickListener(selectedDate -> {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Toast.makeText(MainActivity.this, df.format(selectedDate), Toast.LENGTH_SHORT).show();
        });
    }

    private void initToogle() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initToolbar() {
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitleEnabled(false);
        setSupportActionBar(toolbar);
    }

    private void initNavDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void showFragment(Bundle savedInstanceState) {
        Fragment fragment = findFragmentToShow(savedInstanceState);
        replaceFragment(fragment);
    }


    private Fragment findFragmentToShow(Bundle savedInstanceState) {
        Fragment fragment = null;

        if (savedInstanceState != null) {
            fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        } else {
            fragment = EventsFragment.newInstance();
        }

        return fragment;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        MainFragment selectedFragment = MainFragment.getFragmentByNavDrawerItem(item);

        if (selectedFragment != null) {
            replaceFragmentAndCloseDrawer(selectedFragment.getFragment());
            selectedFragment.setFragmentAndAppBarBehaviour(appBarLayout, nestedScrollViewMain);
            return true;
        } else {
            return false;
        }
    }

    private void replaceFragmentAndCloseDrawer(Fragment fragment) {
        replaceFragment(fragment);
        closeNavDrawer();
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment == null) return;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, FRAGMENT_TAG)
                .commit();
        currentFragment = fragment;
        MainFragment.findFragmentByClass(fragment.getClass()).setFabBottomVisibility(fabBottom);
    }

    private void closeNavDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onPregnancyStartDateChanged() {
        //TODO refresh calendar
    }

    public void setCurrentFragment(Fragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    private enum MainFragment {
        EVENTS(EventsFragment.class, Constants.EXPANDED_APPBAR_ENABLED, !Constants.FAB_BOTTOM_VISIBILE),
        WEIGHT(WeightFragment.class, !Constants.EXPANDED_APPBAR_ENABLED, Constants.FAB_BOTTOM_VISIBILE),
        SETTINGS(SettingsFragment.class, !Constants.EXPANDED_APPBAR_ENABLED, !Constants.FAB_BOTTOM_VISIBILE);

        private Class<? extends Fragment>  fragmentClass;
        private boolean expandedAppBarEnabled;
        private boolean fabBottomVisible;

        MainFragment(Class<? extends Fragment>  fragmentClass, boolean expandedAppBarEnabled, boolean fabBottomVisible) {
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

        public static MainFragment getFragmentByNavDrawerItem(MenuItem item) {
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
            if ( BuildConfig.DEBUG) {
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

        public static MainFragment findFragmentByClass(Class<? extends Fragment> aClass) {
            for (MainFragment mainFragment: MainFragment.values()) {
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
}
