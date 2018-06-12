package com.sdplab.twitterstat.twitterapp4;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.sdplab.twitterstat.database.AppDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChanger.setUpTheme(this);
        setContentView(R.layout.activity_statistics);
        DbAccessAsyncTask dbat = new DbAccessAsyncTask();
        if(!HashtagContainer.getInstance().getTagList().isEmpty()) {
            dbat.execute();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(SettingsFragment.themeChanged)
            recreate();
    }

    private static Map<String, Integer> sortMap(Map<String, Integer> unsortMap) {


        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());


        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    private class DbAccessAsyncTask extends AsyncTask<Void , Void, Map<String,Integer>> {


        @Override
        protected  Map<String,Integer> doInBackground(Void...voids) {
            Twitter twitt = (Twitter) getIntent().getSerializableExtra("Twitter");
            AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
            List<String> users = db.twittDao().getUsers();
            Map<String,Integer> userFreq = new HashMap<>();
            for(String u: users){
                int occurrences = Collections.frequency(users, u);
                userFreq.put(u,occurrences);
            }

            return sortMap(userFreq);
        }

        @Override
        protected void onPostExecute(Map<String,Integer> userFreq) {
            TextView tvUserfreq = findViewById(R.id.userFreq);
            BarChart barchart = findViewById(R.id.barGraph);
            for (Map.Entry<String, Integer> entry : userFreq.entrySet())
            {
                tvUserfreq.append(entry.getKey() + "/" + entry.getValue()+"\n");
            }
        }

    }


}
