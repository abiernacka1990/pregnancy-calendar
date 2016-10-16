package biernacka.pregnancycalendar.ui.fragments.main.dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

import biernacka.pregnancycalendar.App;
import biernacka.pregnancycalendar.R;
import biernacka.pregnancycalendar.adapters.events.BaseEventsAdapter;
import biernacka.pregnancycalendar.adapters.events.UpcomingEventsAdapter;
import biernacka.pregnancycalendar.model.DiaryEntry;
import biernacka.pregnancycalendar.model.Event;
import biernacka.pregnancycalendar.ui.activities.EventCreatorActivity;
import biernacka.pregnancycalendar.ui.activities.MainActivity;
import biernacka.pregnancycalendar.ui.helpers.ImageHelper;
import biernacka.pregnancycalendar.utils.DatesHelper;
import biernacka.pregnancycalendar.utils.Preferences;
import biernacka.pregnancycalendar.utils.StringFormatter;

/**
 * Created by agnieszka on 13.04.16.
 */
public class DashboardFragment extends BaseDashboardFragment implements BaseEventsAdapter.OnItemClickListener {

    public static final int NUMBER_OF_EVENTS = 3;
    public static final int EVENTS_CHANGED_REQUEST = 12;
    private static final long DAY_AS_MILLIS = 1000 * 60 * 60 * 24;
    private TextView dateTextView;
    private TextView calendInfoTextView;
    private View weightView;
    private TextView weightTextView;
    private RecyclerView upcomingEventsListView;
    private UpcomingEventsAdapter upcomingEventsAdapter;
    private TextView upcomingEventsTextView;
    private View upcomingEventsView;
    private TextView diaryTextView;
    private View diaryView;
    private TextView diaryEntryTextView;
    private TextView diaryEntryDateTextView;
    private ImageView diaryPhotoImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstancState) {

        View mainView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initView(mainView);
        return mainView;
    }

    private void initView(View mainView) {
        findViews(mainView);
        setViewsParameters();
        setListeners();
    }

    private void setListeners() {
        weightView.setOnClickListener(view -> gotoWeightFragment());
    }

    private void gotoWeightFragment() {
        ((MainActivity) getActivity()).selectWeightFragment();
    }

    private void gotoDiaryFragment() {
        ((MainActivity) getActivity()).selectDiaryFragment();
    }

    private void gotoCalendarFragment() {
        ((MainActivity) getActivity()).selectCalendarFragment();
    }
    private void findViews(View mainView) {
        dateTextView = (TextView) mainView.findViewById(R.id.textview_date);
        calendInfoTextView = (TextView) mainView.findViewById(R.id.textview_calend_info);
        weightTextView = (TextView) mainView.findViewById(R.id.textview_weight);
        weightView = mainView.findViewById(R.id.layout_weight);
        upcomingEventsListView = (RecyclerView) mainView.findViewById(R.id.layout_upcoming_events);
        upcomingEventsTextView = (TextView) mainView.findViewById(R.id.textview_events);
        upcomingEventsView = mainView.findViewById(R.id.layout_events);
        diaryView = mainView.findViewById(R.id.layout_diary);
        diaryTextView = (TextView) mainView.findViewById(R.id.textview_diary);
        diaryEntryTextView = (TextView) mainView.findViewById(R.id.texview_dashboard_diary_text);
        diaryEntryDateTextView = (TextView) mainView.findViewById(R.id.texview_dashboard_diary_date);
        diaryPhotoImageView = (ImageView) mainView.findViewById(R.id.imageview_diary_entry);
    }

    private void setViewsParameters() {
        initCurrentDate();
        initCalendarInfo();
        initUpcomingEvents();
        initDiary();
        initWeight();
    }

    private void initDiary() {
        diaryView.setOnClickListener(v -> gotoDiaryFragment());
        DiaryEntry recentDiaryEntry = App.getInstance().getDiaryEntriesRepository().getRecentEntry();
        if (recentDiaryEntry == null) {
            initDiaryIfNoEntries();
        } else {
            initDiaryWithEntry(recentDiaryEntry);
        }
    }

    private void initDiaryIfNoEntries() {
        diaryTextView.setText(R.string.dashboard_no_diary_entries);
        diaryEntryDateTextView.setVisibility(View.GONE);
        diaryEntryTextView.setVisibility(View.GONE);
        diaryPhotoImageView.setVisibility(View.GONE);
    }

    private void initDiaryWithEntry(DiaryEntry recentDiaryEntry) {
        diaryTextView.setText(R.string.dashboard_recent_diary_entry);
        diaryEntryDateTextView.setVisibility(View.VISIBLE);
        diaryEntryDateTextView.setText(StringFormatter.withDayOfWeekDate(new DateTime(recentDiaryEntry.getDate())));
        if (recentDiaryEntry.getText() == null || recentDiaryEntry.getText().isEmpty()) {
            diaryEntryTextView.setVisibility(View.GONE);
        } else {
            diaryEntryTextView.setVisibility(View.VISIBLE);
            diaryEntryTextView.setText(recentDiaryEntry.getText());
        }
        Bitmap image = ImageHelper.getBitmap(recentDiaryEntry, getActivity());
        if (image == null) {
            diaryPhotoImageView.setVisibility(View.GONE);
        } else {
            diaryPhotoImageView.setVisibility(View.VISIBLE);
            diaryPhotoImageView.setImageBitmap(image);
        }
    }

    private void initUpcomingEvents() {
        List<Event> upcomingEvents = App.getInstance().getEventsRepository().findUpcomingEvents(DatesHelper.today().getMillis(), NUMBER_OF_EVENTS);
        if (upcomingEvents == null || upcomingEvents.isEmpty()) {
            hideListShowInfo();
        } else {
            showUpcomingList(upcomingEvents);
        }
    }

    private void showUpcomingList(List<Event> upcomingEvents) {
        initRecycleView(upcomingEvents);
        upcomingEventsTextView.setText(R.string.dashboard_upcoming_events);
        upcomingEventsView.setOnClickListener(v -> gotoCalendarFragment());

    }

    private void hideListShowInfo() {
        upcomingEventsListView.setVisibility(View.GONE);
        upcomingEventsTextView.setText(R.string.dashboard_no_upcoming_events);
        upcomingEventsView.setOnClickListener(v -> openNewEventActivity());
    }

    private void openNewEventActivity() {

        Intent startEventCreatorActIntent = new Intent(getContext(), EventCreatorActivity.class);
        startEventCreatorActivityForResult(startEventCreatorActIntent);
    }

    private void startEventCreatorActivityForResult(Intent startEventCreatorActIntent) {
        startActivityForResult(startEventCreatorActIntent, EVENTS_CHANGED_REQUEST);
    }


    private void initRecycleView(List<Event> upcomingEvents) {
        upcomingEventsListView.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        upcomingEventsListView.setLayoutManager(layoutManager);
        upcomingEventsAdapter = new UpcomingEventsAdapter(getContext(), upcomingEvents, this);
        upcomingEventsListView.setAdapter(upcomingEventsAdapter);
    }

    private void initCurrentDate() {
        String currentDate = StringFormatter.date(new Date());
        dateTextView.setText(currentDate);
    }

    private void initCalendarInfo() {
        String calendarInfo = new CalendarInfo().getInfoText();
        calendInfoTextView.setText(calendarInfo);

    }

    private void initWeight() {

        if (DatesHelper.currentTimeIsInPregnancy()) {
            weightView.setVisibility(View.VISIBLE);
            setWeightText();
        } else {
            weightView.setVisibility(View.GONE);
        }
    }

    private void setWeightText() {
        Integer currentWeek = DatesHelper.getCurrentWeek();
        double weight = 0;
        boolean weightForCurrentWeekExist =
                currentWeek != null && App.getInstance().getWeightRepository().exist(currentWeek);

        if (weightForCurrentWeekExist) {
            try {
                weight = App.getInstance().getWeightRepository().getWeightForWeek(currentWeek);
            } catch (Exception e) {
                e.printStackTrace();
                weightForCurrentWeekExist = false;
            }
        }

        if (weightForCurrentWeekExist) {
            String weightInfo = String.format(getString(R.string.dashboard_weight), weight);
            weightTextView.setText(weightInfo);
        } else {
            weightTextView.setText(R.string.dashboard_weight_add);
        }
    }

    @Override
    public void onItemClicked(Event event) {
        Intent startEventCreatorActIntent = new Intent(getContext(), EventCreatorActivity.class);
        startEventCreatorActIntent.putExtra(EventCreatorActivity.ARG_EVENT_ID, event.getId());
        startEventCreatorActivityForResult(startEventCreatorActIntent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case EVENTS_CHANGED_REQUEST:
                if (resultCode == getActivity().RESULT_OK) {
                    initUpcomingEvents();
                    ((MainActivity) getActivity()).refreshCalendar();
                }
                break;
        }
    }

    private class CalendarInfo {

        private final DateTime today;
        private final DateTime planndedBirthDate;
        private final Integer currentWeek;
        private final long daysToOrAfterPlannedBirthDate;

        public CalendarInfo() {
            today = DatesHelper.today();
            planndedBirthDate = new DateTime(Preferences.getInstance().getPlannedBirthDate()).withTimeAtStartOfDay();
            currentWeek = DatesHelper.getCurrentWeek();
            daysToOrAfterPlannedBirthDate =  Math.abs((today.getMillis() - planndedBirthDate.getMillis()) / DAY_AS_MILLIS);
        }

        public String getInfoText() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(");

            appendWeekInfo(stringBuilder);
            appendDayInfo(stringBuilder);

            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        private void appendDayInfo(StringBuilder stringBuilder) {
            if (isPlannedDateOfBirth()) {
                stringBuilder.append(getString(R.string.dashboard_calend_info_big_day));
            } else {
                appendInfoAboutAfterOrBeforeDate(stringBuilder);
            }
        }

        private void appendInfoAboutAfterOrBeforeDate(StringBuilder stringBuilder) {
            String afterOrBeforePlannedBirthDateText;
            if (isPlannedBirthDateAfterToday()) {
                afterOrBeforePlannedBirthDateText = getString(R.string.dashboard_calend_info_after);
            } else {
                afterOrBeforePlannedBirthDateText = getString(R.string.dashboard_calend_info_before);
            }

            String dayInfo = getString(R.string.dashboard_calend_info_day_info);
            String validFormOfDay = getValidFormOfDay(daysToOrAfterPlannedBirthDate);
            stringBuilder.append(String.format(dayInfo, daysToOrAfterPlannedBirthDate, validFormOfDay, afterOrBeforePlannedBirthDateText));
        }

        private boolean isPlannedBirthDateAfterToday() {
            return planndedBirthDate.getMillis() < today.getMillis();
        }

        private boolean isPlannedDateOfBirth() {
            return new DateTime(planndedBirthDate).withTimeAtStartOfDay().getMillis() == today.getMillis();
        }

        private void appendWeekInfo(StringBuilder stringBuilder) {
            if (currentWeek != null) {
                String weekInfo = getString(R.string.dashboard_calend_info_week_info);
                stringBuilder.append(String.format(weekInfo, currentWeek.intValue()));
            }
        }

        private String getValidFormOfDay(long day) {
            return day > 1 ? getString(R.string.dashboard_calend_info_days) : getString(R.string.dashboard_calend_info_day);
        }
    }
}
