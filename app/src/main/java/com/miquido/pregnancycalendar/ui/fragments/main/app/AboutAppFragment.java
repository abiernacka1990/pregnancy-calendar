package com.miquido.pregnancycalendar.ui.fragments.main.app;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miquido.pregnancycalendar.R;
import com.miquido.pregnancycalendar.ui.fragments.main.MainFragment;

/**
 * Created by agnieszka on 14.04.16.
 */
public class AboutAppFragment extends MainFragment {

    public AboutAppFragment() {
        //required
    }

    public static MainFragment newInstance() {
        return new AboutAppFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_about_app, container, false);
        initView(mainView);
        return mainView;
    }

    private void initView(View mainView) {
        mainView.findViewById(R.id.textview_rate_us).setOnClickListener(rateUsOnClickListener);
        mainView.findViewById(R.id.imageview_googleplay).setOnClickListener(rateUsOnClickListener);
    }

    private View.OnClickListener rateUsOnClickListener = view -> {
        startRateIntent();
    };

    private void startRateIntent() {
        Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
        }
    }

    @Override
    public boolean isExpandedAppBarEnabled() {
        return false;
    }

    @Override
    public boolean isFabBottomVisible() {
        return false;
    }
}
