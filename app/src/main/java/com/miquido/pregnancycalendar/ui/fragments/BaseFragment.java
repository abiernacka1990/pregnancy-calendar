package com.miquido.pregnancycalendar.ui.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by agnieszka on 03.11.15.
 *
 * Base fragment class for application.
 */
public abstract class BaseFragment extends Fragment {

    protected AppCompatActivity getAppCompatActivity() {
        return  (AppCompatActivity) getActivity();
    }
}
