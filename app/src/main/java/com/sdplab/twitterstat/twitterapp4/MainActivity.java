package com.sdplab.twitterstat.twitterapp4;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private TwitterService mTwitterService;
    Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button one = findViewById(R.id.button1);
        Button two = findViewById(R.id.button2);
        Button three = findViewById(R.id.button3);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        mTwitterService = new TwitterService(this);
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
        }
    }

    private void startTwitterService(){
        mServiceIntent = new Intent(this, mTwitterService.getClass());
        if (!isMyServiceRunning(mTwitterService.getClass())) {
            startService(mServiceIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
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

    private void connectToAPI(ConfigurationBuilder cb){
        cb.setDebugEnabled(true).setOAuthConsumerKey(Constants.consumerKey)
                .setOAuthConsumerSecret(Constants.consumerSecret)
                .setOAuthAccessToken(Constants.accessToken)
                .setOAuthAccessTokenSecret(Constants.accessSecret);
        TwitterFactory tfactory = new TwitterFactory(cb.build());
        twitt = tfactory.getInstance();
    }


    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }

}
