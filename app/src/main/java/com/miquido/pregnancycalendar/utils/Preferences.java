package com.miquido.pregnancycalendar.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.miquido.pregnancycalendar.App;

/**
 * Created by agnieszka on 03.11.15.
 */
public class Preferences {

    private static final String PREFERENCES_NAME = "PREGNANCY CALENDAR SHARED PREFERENCES";
    private static final String PREGNANCY_START_DATE_KEY = "PREGNANCY_START_DATE_KEY";
    private static final String WEIGHT_UNIT = "WEIGHT_UNIT";

    private static Preferences ourInstance = new Preferences();

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;

    public static Preferences getInstance() {
        return ourInstance;
    }

    private Preferences() {
        sharedpreferences = App.getInstance().getApplicationContext()
                .getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    public long getPregnancyStartDate() {
        return sharedpreferences.getLong(PREGNANCY_START_DATE_KEY, 0);
    }

    public void setPregnancyStartDate(long date) {
        editor.putLong(PREGNANCY_START_DATE_KEY, date);
        editor.apply();
    }

    public String getWeightUnit() {
        return sharedpreferences.getString(WEIGHT_UNIT, App.getInstance().getDefaultWeightUnit());
    }

    public void setWeightUnit(String weightUnit) {
        editor.putString(WEIGHT_UNIT, weightUnit);
        editor.apply();
    }


}
