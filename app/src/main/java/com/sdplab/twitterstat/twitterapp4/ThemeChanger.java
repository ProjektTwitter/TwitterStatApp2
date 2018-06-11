package com.sdplab.twitterstat.twitterapp4;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;

public class ThemeChanger {

    public static void setUpTheme(AppCompatActivity activity){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean theme = sharedPref.getBoolean("switch_preference_2", false);
        if(!theme){
            activity.setTheme(R.style.AppTheme);}
        else{activity.setTheme(R.style.AppTheme2);}
    }

}
