package com.miquido.pregnancycalendar.utils;

import android.app.Activity;
import android.os.Build;

/**
 * Created by agnieszka on 07.01.16.
 */
public final class UsefulUIMethods {

    public static int getColor(int resId, Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           return activity.getResources().getColor(resId, activity.getTheme());
        }else {
           return activity.getResources().getColor(resId);
        }
    }
}
