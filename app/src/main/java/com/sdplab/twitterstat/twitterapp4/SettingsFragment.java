package com.sdplab.twitterstat.twitterapp4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static boolean themeChanged = false;
    public static boolean killTwitterService= false;
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        switch(key){

            case "switch_preference_2":

                themeChanged = true;
                getActivity().recreate();
                break;

            case "switch_preference_1":

                boolean result = sharedPreferences.getBoolean("switch_preference_1",false );
                Intent intent1 = new Intent(getActivity(), TwitterService.class);

                if(!result){

                    killTwitterService = true;

                }

                if (!TwitterService.isRunning) {
                    getActivity().startService(intent1);
                }

                break;



            case "list_preference_1":
                Intent intent2 = new Intent(getActivity(), TwitterService.class);
                if (TwitterService.isRunning) {
                    getActivity().stopService(intent2);
                }
                break;

            case "list_preference_2":
                Intent intent3 = new Intent(getActivity(), TwittSaverService.class);
                if (TwitterService.isRunning) {
                    getActivity().stopService(intent3);
                }
                break;

        }
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
            addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}

