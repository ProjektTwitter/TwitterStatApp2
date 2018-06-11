package com.sdplab.twitterstat.twitterapp4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static boolean ThemeChanged = false;
    public static boolean FrequencyChanged = false;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        switch(key){

            case "switch_preference_2":

                ThemeChanged = true;
                getActivity().recreate();
                break;

            case "switch_preference_1":

                Intent intent1 = new Intent(getActivity(), TwitterService.class);
                if (!TwitterService.isRunning) {
                    getActivity().startService(intent1);
                }
                break;

            case "list_preference_1":
                Intent intent2 = new Intent(getActivity(), TwitterService.class);
                if (TwitterService.isRunning) {
                    getActivity().stopService(intent2);
                }
                FrequencyChanged = true;
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
