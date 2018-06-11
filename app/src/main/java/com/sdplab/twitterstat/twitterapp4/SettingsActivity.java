package com.sdplab.twitterstat.twitterapp4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChanger.setUpTheme(this);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            Fragment preferenceFragment = new SettingsFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.prefLayout, preferenceFragment);
            ft.commit();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(SettingsFragment.ThemeChanged)

            recreate();

    }

}
