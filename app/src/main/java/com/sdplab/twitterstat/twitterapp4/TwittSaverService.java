package com.sdplab.twitterstat.twitterapp4;

import android.app.Service;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.sdplab.twitterstat.database.AppDatabase;
import com.sdplab.twitterstat.database.Twitt;
import com.sdplab.twitterstat.database.TwittDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwittSaverService extends Service {

    private Timer timer;
    private TimerTask timerTask;
    private static final String TAG = "twittSaver";
    private Intent tIntent;
    private AppDatabase db;
    private Twitter twitter;
    private int freq = 900000;

    public TwittSaverService() {

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        tIntent = intent;
        db = AppDatabase.getAppDatabase(this);
        twitter = (Twitter) tIntent.getSerializableExtra("Twitter");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String fSettings = sharedPref.getString("list_preference_2", "");
        switch(fSettings){

            case "15 minutes":
                freq = 900000;
                break;

            case "30 minutes":
                freq = 1800000;
                break;

            case "1 hour":
                freq = 3200000;
                break;

            case "3 hours":
                freq = 9600000;
                break;

            case "6 hours":
                freq = 19200000;
                break;

            case "12 hours":
                freq = 28400000;
                break;

            case "24 hours":
                freq = 56800000;
                break;
        }
        startTimer();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;

    }

    public void getTwitts(){
        ArrayList<List<twitter4j.Status>> status = new ArrayList<List<twitter4j.Status>>();
        Query query;
        QueryResult result;
        if (!HashtagContainer.getInstance().getTagList().isEmpty()) {
            for (String q : HashtagContainer.getInstance().getTagList()) {
                query = new Query(q);
                try {
                    result = twitter.search(query);
                    status.add(result.getTweets());
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
        }
        saveToDatabase(db,status );
     }

    private static void saveToDatabase(AppDatabase db,ArrayList<List<twitter4j.Status>> status) {
        List<Twitt> cTwittList =db.twittDao().getAll();
        for (List<twitter4j.Status>sl : status) {
            for (twitter4j.Status s : sl) {
                boolean isInBase = false;
                String user = s.getUser().getName();
                String text = s.getText();
                String date = s.getCreatedAt().toString();

                for(Twitt t:cTwittList){
                    if(t.getUser().equals(user)&&t.getText().equals(text)&&t.getDate().equals(date)){
                        isInBase = true;
                    }
                }
                if(!isInBase) {
                    Twitt twitt = new Twitt();
                    twitt.setUser(user);
                    twitt.setText(text);
                    twitt.setDate(date);
                    twitt.setFcount(s.getFavoriteCount());
                    twitt.setRcount(s.getRetweetCount());
                    addTwitt(db, twitt);
                }
            }
        }
    }

    private static Twitt addTwitt(final AppDatabase db, Twitt twitt ) {
        db.twittDao().insertAll(twitt);

        return twitt;
    }

    public void startTimer() {
        timer = new Timer();

        initializeTimerTask();

        timer.schedule(timerTask, 1000, freq); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                getTwitts();
                Log.i(TAG, "Twitts saved to database");
            }
        };
    }

    public void stoptimertask() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

            Intent broadcastIntent = new Intent(".TwittSaverServiceRestarterBroadcastReceiver");
            sendBroadcast(broadcastIntent);
            stoptimertask();

    }

}
