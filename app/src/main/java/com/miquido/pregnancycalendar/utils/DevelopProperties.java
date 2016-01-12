package com.miquido.pregnancycalendar.utils;

import android.util.Log;

import com.miquido.pregnancycalendar.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by agnieszka on 12.01.16.
 */
public class DevelopProperties {

    private static final String DEVELOP_PROPERTIES_PATH = "/develop.properties";
    private static final String FALSE_VALUE = "false";
    private final Properties properties = new Properties();

    public DevelopProperties() {
        try {

            InputStream stream = DevelopProperties.class.getResourceAsStream(DEVELOP_PROPERTIES_PATH);

            if (stream != null) {
                properties.load(stream);
            }
        } catch (IOException e) {

            if (BuildConfig.DEBUG) {
                Log.w("DevelopProperties", "Unable to access develop.properties file", e);
            }
        }
    }

    public boolean useFakeEventDatabase() {
        return Boolean.valueOf(properties.getProperty("useFakeEventDatabase", FALSE_VALUE));
    }

    public boolean useFakeWeightDatabase() {
        return Boolean.valueOf(properties.getProperty("useFakeWeightDatabase", FALSE_VALUE));
    }


}
