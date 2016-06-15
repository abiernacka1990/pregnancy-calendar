package biernacka.pregnancycalendar.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import biernacka.pregnancycalendar.BuildConfig;
import biernacka.pregnancycalendar.R;
import biernacka.pregnancycalendar.ui.decorators.PregnancyDayDecorator;
import biernacka.pregnancycalendar.ui.fragments.BaseFragment;
import biernacka.pregnancycalendar.ui.fragments.event.EventEditFragment;
import biernacka.pregnancycalendar.ui.fragments.main.app.AboutAppFragment;
import biernacka.pregnancycalendar.ui.fragments.main.features.DiaryFragment;
import biernacka.pregnancycalendar.ui.fragments.main.dashboard.DashboardFragmentFactory;
import biernacka.pregnancycalendar.ui.fragments.main.features.EventsFragment;
import biernacka.pregnancycalendar.ui.fragments.main.MainFragment;
import biernacka.pregnancycalendar.ui.fragments.main.app.SettingsFragment;
import biernacka.pregnancycalendar.ui.fragments.main.features.WeightFragment;
import com.samsistemas.calendarview.widget.CalendarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.OnSettingsChangesListener {

    public static final int EVENTS_CHANGED_REQUEST = 45;
    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    private static final String TAG = "MainActivity";
    private static final String SELECTED_DATE = "SELECTED_DATE";

    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout toolbarLayout;
    private CalendarView calendarView;
    private NestedScrollView nestedScrollViewMain;
    private MainFragment currentFragment;
    private FloatingActionButton fabBottom, fabTop;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setFragmentAndAppBarBehaviour(currentFragment);
    }

    private void initView(Bundle savedInstanceState) {
        initToolbar();
        initToogle();
        initNavDrawer();
        initContent();
        initCalendar(savedInstanceState);
        showFragment(savedInstanceState);
    }

    private void initContent() {
        nestedScrollViewMain = (NestedScrollView) findViewById(R.id.nested_scrollview_main);
        fabBottom = (FloatingActionButton) findViewById(R.id.fab_bottom);
        fabBottom.setOnClickListener(view -> sendOnBottmFabClickEventToFragment());
    }

    private void sendOnBottmFabClickEventToFragment() {
        if (currentFragment instanceof BaseFragment) {
            ((BaseFragment) currentFragment).onBottomFloatingBtClick();
        }
    }

    private void initCalendar(Bundle savedInstanceState) {

        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        calendarView.setDecoratorsList(new ArrayList<>(Arrays.asList(new PregnancyDayDecorator())));
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        calendarView.setIsOverflowDateVisible(true);
        calendarView.refreshCalendar(Calendar.getInstance(Locale.getDefault()));
        if (savedInstanceState != null && savedInstanceState.getLong(SELECTED_DATE, 0) > 0) {
            long selectedDateFromSavedIS = savedInstanceState.getLong(SELECTED_DATE, 0);
            calendarView.setDateAsSelected(new Date(selectedDateFromSavedIS));
        } else {
            calendarView.setDateAsSelected(Calendar.getInstance().getTime());
        }
        calendarView.setOnDateClickListener(selectedDate -> updateEventsList());
    }

    private void refreshCalendar() {
        Date dateToSelect = calendarView.getLastSelectedDay() != null ? calendarView.getLastSelectedDay() : Calendar.getInstance().getTime();
        calendarView.refreshCalendar(Calendar.getInstance(Locale.getDefault()));
        calendarView.setDateAsSelected(dateToSelect);
    }

    private void updateEventsList() {
        if (currentFragment instanceof EventsFragment) {
            ((EventsFragment) currentFragment).updateEventList(calendarView.getLastSelectedDay());
        }
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
        fabTop = (FloatingActionButton) findViewById(R.id.fab_top);
        fabTop.setOnClickListener(view -> openEventCreatorActivityWithSelectedDate());
    }

    private void openEventCreatorActivityWithSelectedDate() {
        Intent startEventCreatorActIntent = new Intent(this, EventCreatorActivity.class);
        startEventCreatorActIntent.putExtra(EventEditFragment.ARG_EVENT_START_DATE, calendarView.getLastSelectedDay().getTime());
        startActivityForEventsChangedResult(startEventCreatorActIntent);
    }

    private void startActivityForEventsChangedResult(Intent startEventCreatorActIntent) {
        startActivityForResult(startEventCreatorActIntent, EVENTS_CHANGED_REQUEST);
    }

    private void initNavDrawer() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void showFragment(Bundle savedInstanceState) {
        Fragment fragment = findFragmentToShow(savedInstanceState);
        replaceFragment(fragment);
    }


    private Fragment findFragmentToShow(Bundle savedInstanceState) {
        Fragment fragment;

        if (savedInstanceState != null) {
            fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        } else {
            fragment = DashboardFragmentFactory.newInstance();
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

        MainFragment selectedFragment = getFragmentByNavDrawerItem(item);

        if (selectedFragment != null) {
            replaceFragmentAndCloseDrawer(selectedFragment);
            return true;
        } else {
            return false;
        }
    }

    private void updateActivityViewForSelectedFragment(MainFragment selectedFragment) {
        setFragmentAndAppBarBehaviour(selectedFragment);
        setFabBottomVisibility(selectedFragment);
    }

    private void setFragmentAndAppBarBehaviour(MainFragment selectedFragment) {
        boolean isExpandedAppBarEnabled = selectedFragment.isExpandedAppBarEnabled();
        appBarLayout.setExpanded(isExpandedAppBarEnabled, false);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        if (params.getBehavior() == null) {
            params.setBehavior(new AppBarLayout.Behavior() {});
        }
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return isExpandedAppBarEnabled;
            }
        });
        nestedScrollViewMain.setNestedScrollingEnabled(isExpandedAppBarEnabled);
    }

    public void setFabBottomVisibility(MainFragment selectedFragment) {
        boolean isFabBottomVisible = selectedFragment.isFabBottomVisible();
        if (isFabBottomVisible) {
            fabBottom.setVisibility(View.VISIBLE);
        } else {
            fabBottom.setVisibility(View.GONE);
        }
    }

    private void replaceFragmentAndCloseDrawer(Fragment fragment) {
        replaceFragment(fragment);
        closeNavDrawer();
    }

    private void replaceFragment(Fragment fragment) {
        if (fragment == null) return;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nested_scrollview_main, fragment, FRAGMENT_TAG)
                .commit();
        currentFragment = (MainFragment) fragment;
        updateActivityViewForSelectedFragment(currentFragment);
    }

    private void closeNavDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onPlannedBirthDateChanged() {
        refreshCalendar();
    }

    public MainFragment getFragmentByNavDrawerItem(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            return DashboardFragmentFactory.newInstance();
        } else if (id == R.id.nav_settings) {
            return SettingsFragment.newInstance();
        } else if (id == R.id.nav_weight) {
            return WeightFragment.newInstance();
        } else if (id == R.id.nav_calendar) {
            return EventsFragment.newInstance(calendarView.getLastSelectedDay());
        } else if (id == R.id.nav_diary) {
            return DiaryFragment.newInstance();
        } else if (id == R.id.nav_about_app) {
            return AboutAppFragment.newInstance();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EVENTS_CHANGED_REQUEST) {
            if (resultCode == RESULT_OK) {
                refreshCalendar();
                updateEventsList();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(SELECTED_DATE, calendarView.getLastSelectedDay().getTime());
        super.onSaveInstanceState(savedInstanceState);
    }

    public void goToSettings() {
        navigationView.setCheckedItem(R.id.nav_settings);
        replaceFragment(SettingsFragment.newInstance());
    }
}
