package com.sdplab.twitterstat.twitterapp4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sdplab.twitterstat.utils.Constants;

import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button one = findViewById(R.id.button1);
        one.setOnClickListener(this);
        ConfigurationBuilder cb = new ConfigurationBuilder();
        connectToAPI(cb);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.button1:
                startAddHashActivity();
                break;

        }
    }

    private void startAddHashActivity(){
        Intent intent = new Intent(this, AddHashActivity.class);
        startActivity(intent);
    }

    private void connectToAPI(ConfigurationBuilder cb){
        cb.setDebugEnabled(true).setOAuthConsumerKey(Constants.consumerKey)
                .setOAuthConsumerSecret(Constants.consumerSecret)
                .setOAuthAccessToken(Constants.accessToken)
                .setOAuthAccessTokenSecret(Constants.accessSecret);
    }

}
