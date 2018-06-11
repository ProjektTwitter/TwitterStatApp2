package com.sdplab.twitterstat.twitterapp4;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class TwitterService extends Service {

    public static boolean isRunning = false;

    public TwitterService(Context applicationContext) {
        super();
        Log.i("TwitterService", "service initialized");
    }

    public TwitterService() {
    }

    private int freq = 900000;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        isRunning = true;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String fSettings = sharedPref.getString("list_preference_1", "");
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
        System.out.println(fSettings);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent(".TwitterServiceRestarterBroadcastReceiver");
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    public void setUpNotification(){
        Intent intent = new Intent(this, ShowTwittsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_markunread)
                .setContentTitle("We got new twitts for you!")
                .setContentText("Click to see the list of twitts")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Notification not = mBuilder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, not );

    }

    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        timer = new Timer();

        initializeTimerTask();

        timer.schedule(timerTask, 1000, freq); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("TwitterService", "Notification sent");
                setUpNotification();
            }
        };
    }

    public void stoptimertask() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;

    }
}
