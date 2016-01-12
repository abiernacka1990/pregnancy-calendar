package com.miquido.pregnancycalendar;

import android.app.Application;

import com.miquido.pregnancycalendar.db.EventsRepository;
import com.miquido.pregnancycalendar.db.WeightRepository;
import com.miquido.pregnancycalendar.db.ormlite.OrmEventRepository;
import com.miquido.pregnancycalendar.db.ormlite.OrmWeightRepository;
import com.miquido.pregnancycalendar.utils.DevelopProperties;

/**
 * Created by agnieszka on 03.11.15.
 *
 * Application
 */
public class App extends Application {

    private static App instance;
    private WeightRepository weightRepository;
    private EventsRepository eventsRepository;


    public static App getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        weightRepository = new OrmWeightRepository(getApplicationContext());
            eventsRepository = new OrmEventRepository(getApplicationContext());
    }

    public WeightRepository getWeightRepository() {
        return weightRepository;
    }

    public EventsRepository getEventsRepository() {
        return eventsRepository;
    }

    public String getDefaultWeightUnit() {
        return getApplicationContext().getString(R.string.default_weight_unit);
    }
}
