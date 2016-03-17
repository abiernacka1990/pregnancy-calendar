package com.miquido.pregnancycalendar.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.miquido.pregnancycalendar.ui.fragments.PregnancyCaldroidFragment;
import com.miquido.pregnancycalendar.ui.fragments.SettingsFragment;
import com.miquido.pregnancycalendar.ui.fragments.WeightFragment;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.OnSettingsChangesListener {

    private static final String FRAGMENT_TAG = "FragmentTag";
    private static final String TAG = "MainActivity";
    private CaldroidFragment caldroidFragment = new PregnancyCaldroidFragment();;
    private Toolbar toolbar;

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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initNavDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void showFragment(Bundle savedInstanceState) {
        Fragment fragment = findFragmentToShow(savedInstanceState);

        replaceMainFragment(fragment);
    }

    private Fragment findFragmentToShow(Bundle savedInstanceState) {
        Fragment fragment;

        if (savedInstanceState != null) {
            fragment = restoreFragment(savedInstanceState);
        } else {
            initCaldroidFragment();
            fragment = caldroidFragment;
        }

        return fragment;
    }

    private void initCaldroidFragment() {
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

        caldroidFragment.setArguments(args);
    }

    private Fragment restoreFragment(Bundle savedInstanceState) {
        Fragment fragment;
        fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment instanceof CaldroidFragment) {
            fragment = caldroidFragment;
        }
        caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
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

        Fragment fragment = getFragmentForSelectedNavDrawerItem(item);

        if (fragment != null) {
            replaceMainFragmentAndCloseDrawer(fragment);
            return true;
        } else {
            return false;
        }
    }

    private void replaceMainFragmentAndCloseDrawer(Fragment fragment) {
        replaceMainFragment(fragment);
        closeNavDrawer();
    }

    private void replaceMainFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_fragments_container, fragment, FRAGMENT_TAG)
                .commit();
    }

    private void closeNavDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Nullable
    private Fragment getFragmentForSelectedNavDrawerItem(MenuItem item) {
        Fragment fragment;
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            fragment = SettingsFragment.newInstance();
        } else if (id == R.id.nav_weight) {
            fragment = WeightFragment.newInstance();
        } else if (id == R.id.nav_calendar) {
            fragment = caldroidFragment;
        } else {
            logNoItemFound(item);
            fragment = null;
        }
        return fragment;
    }

    private void logNoItemFound(MenuItem item) {
        if ( BuildConfig.DEBUG) {
            Log.d(TAG, "No fragment specified for selected item: " + item.toString());
        }
    }

    @Override
    public void onPregnancyStartDateChanged(long date) {
        //TODO refresh calendar
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
    }
}
