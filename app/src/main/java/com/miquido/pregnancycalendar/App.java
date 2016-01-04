package com.miquido.pregnancycalendar;

import android.app.Application;

import com.miquido.pregnancycalendar.db.WeightRepository;
import com.miquido.pregnancycalendar.db.ormlite.OrmWeightRepository;

/**
 * Created by agnieszka on 03.11.15.
 *
 * Application
 */
public class App extends Application {

    private static App instance;
    private WeightRepository weightRepository;


    public static App getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        weightRepository = new OrmWeightRepository(getApplicationContext());
    }

    public WeightRepository getWeightRepository() {
        return weightRepository;
    }

    public String getDefaultWeightUnit() {
        return getApplicationContext().getString(R.string.default_weight_unit);
    }
}
