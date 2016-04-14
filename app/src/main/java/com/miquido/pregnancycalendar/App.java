package com.miquido.pregnancycalendar;

import android.app.Application;

import com.miquido.pregnancycalendar.db.DiaryEntriesRepository;
import com.miquido.pregnancycalendar.db.EventsRepository;
import com.miquido.pregnancycalendar.db.WeightRepository;
import com.miquido.pregnancycalendar.db.fake.FakeEventRepository;
import com.miquido.pregnancycalendar.db.fake.FakeWeightRepository;
import com.miquido.pregnancycalendar.db.ormlite.OrmDiaryEntryRepository;
import com.miquido.pregnancycalendar.db.ormlite.OrmEventRepository;
import com.miquido.pregnancycalendar.db.ormlite.OrmWeightRepository;
import com.miquido.pregnancycalendar.model.DiaryEntry;
import com.miquido.pregnancycalendar.utils.DevelopProperties;

import java.util.List;

/**
 * Created by agnieszka on 03.11.15.
 * <p>
 * Application
 */
public class App extends Application {

    private static App instance;
    private WeightRepository weightRepository;
    private EventsRepository eventsRepository;
    private DiaryEntriesRepository diaryEntriesRepository;
    private DevelopProperties properties = new DevelopProperties();


    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (properties.useFakeWeightDatabase()) {
            weightRepository = new FakeWeightRepository();
        } else {
            weightRepository = new OrmWeightRepository(getApplicationContext());
        }
        if (properties.useFakeEventDatabase()) {
            eventsRepository = new FakeEventRepository();
        } else {
            eventsRepository = new OrmEventRepository(getApplicationContext());
        }
        diaryEntriesRepository = new OrmDiaryEntryRepository(getApplicationContext());
    }

    public WeightRepository getWeightRepository() {
        return weightRepository;
    }

    public EventsRepository getEventsRepository() {
        return eventsRepository;
    }

    public DiaryEntriesRepository getDiaryEntriesRepository() {
        return diaryEntriesRepository;
    }

    public String getDefaultWeightUnit() {
        return getApplicationContext().getString(R.string.default_weight_unit);
    }
}
