package com.miquido.pregnancycalendar;

import android.app.Application;

import com.miquido.pregnancycalendar.db.ormlite.WeightInfoRepository;

/**
 * Created by agnieszka on 03.11.15.
 *
 * Application
 */
public class App extends Application {

    private static App instance;
    private WeightInfoRepository weightInfoRepository;
    public static App getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        weightInfoRepository = new WeightInfoRepository(getApplicationContext());
    }

    public WeightInfoRepository getWeightInfoRepository() {
        return weightInfoRepository;
    }
}
