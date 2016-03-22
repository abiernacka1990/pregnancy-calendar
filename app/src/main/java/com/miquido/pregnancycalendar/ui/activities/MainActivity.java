package com.miquido.pregnancycalendar.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.miquido.pregnancycalendar.BuildConfig;
import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.model.Weight;
import com.miquido.pregnancycalendar.ui.fragments.SettingsFragment;
import com.miquido.pregnancycalendar.ui.fragments.WeightFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.OnSettingsChangesListener {

    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout toolbarLayout;

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
        showFragment(savedInstanceState);
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
            //TODO
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
            selectedFragment.setAppBarBehaviour(appBarLayout);
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
    }

    private void closeNavDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onPregnancyStartDateChanged() {
        //TODO refresh calendar
    }

    private enum MainFragment {
        WEIGHT(WeightFragment.class, false),
        SETTINGS(SettingsFragment.class, false);

        private Class<? extends Fragment>  fragmentClass;
        private boolean expandedAppBarEnabled;

        MainFragment(Class<? extends Fragment>  fragmentClass, boolean expandedAppBarEnabled) {
            this.fragmentClass = fragmentClass;
            this.expandedAppBarEnabled = expandedAppBarEnabled;
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

        public void setAppBarBehaviour(AppBarLayout appBarLayout) {
            appBarLayout.setExpanded(expandedAppBarEnabled, false);
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
            behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return expandedAppBarEnabled;
                }
            });
        }

        public static MainFragment getFragmentByNavDrawerItem(MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.nav_settings) {
                return SETTINGS;
            } else if (id == R.id.nav_weight) {
                return WEIGHT;
            } else if (id == R.id.nav_calendar) {
                return null;
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
    }
}
