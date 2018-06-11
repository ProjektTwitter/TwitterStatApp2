package com.sdplab.twitterstat.twitterapp4;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sdplab.twitterstat.utils.Constants;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static Twitter twitt;
    private static final String TAG = "mainActivity";
    Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Creating!");
        super.onCreate(savedInstanceState);
        ThemeChanger.setUpTheme(this);
        setContentView(R.layout.activity_main);
        Button one = findViewById(R.id.button1);
        Button two = findViewById(R.id.button2);
        Button three = findViewById(R.id.button3);
        Button four = findViewById(R.id.button4);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        ConfigurationBuilder cb = new ConfigurationBuilder();
        connectToAPI(cb);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.button1:
                startAddHashActivity();
                break;

            case R.id.button2:
                startShowTwittsActivity();
                break;

            case R.id.button3:
                startTwitterService();
                break;

            case R.id.button4:
                startSettingsActivity();
                break;


        }
    }

    protected void startTwitterService(){
        mServiceIntent = new Intent(this, TwitterService.class);
        if (!TwitterService.isRunning) {
            startService(mServiceIntent);
        }
    }


    private void startAddHashActivity(){
        Intent intent = new Intent(this, AddHashActivity.class);
        startActivity(intent);
    }

    private void startShowTwittsActivity(){
        Intent intent = new Intent(this, ShowTwittsActivity.class);
        intent.putExtra("Twitter", twitt);
        startActivity(intent);
    }

    private void startSettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void connectToAPI(ConfigurationBuilder cb){
        cb.setDebugEnabled(true).setOAuthConsumerKey(Constants.consumerKey)
                .setOAuthConsumerSecret(Constants.consumerSecret)
                .setOAuthAccessToken(Constants.accessToken)
                .setOAuthAccessTokenSecret(Constants.accessSecret);
        TwitterFactory tfactory = new TwitterFactory(cb.build());
        twitt = tfactory.getInstance();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(SettingsFragment.ThemeChanged)

            recreate();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (TwitterService.isRunning) {
            stopService(mServiceIntent);
        }
    }



}
