package com.sdplab.twitterstat.twitterapp4;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sdplab.twitterstat.database.AppDatabase;
import com.sdplab.twitterstat.database.Twitt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChanger.setUpTheme(this);
        setContentView(R.layout.activity_statistics);
        DbUsersAsyncTask dbUsersAsyncTask = new DbUsersAsyncTask();
        if(!HashtagContainer.getInstance().getTagList().isEmpty()) {
            dbUsersAsyncTask.execute();
        }
        else{
                CharSequence text = "There is not enough data";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(this, text, duration).show();
        }
        DbFavTwittsAsyncTask dbFavTwittsAsyncTask = new DbFavTwittsAsyncTask();
        if(!HashtagContainer.getInstance().getTagList().isEmpty()) {
            dbFavTwittsAsyncTask.execute();
        }
        DbRetTwittsAsyncTask dbRetTwittsAsyncTask1 = new DbRetTwittsAsyncTask();
        if(!HashtagContainer.getInstance().getTagList().isEmpty()) {
            dbRetTwittsAsyncTask1.execute();
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

    private class DbUsersAsyncTask extends AsyncTask<Void , Void, Map<String,Integer>> {


        @Override
        protected  Map<String,Integer> doInBackground(Void...voids) {

            AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
            List<String> users = db.twittDao().getUsers();
            Map<String,Integer> userFreq = new LinkedHashMap<>();
            for(String u: users){
                int occurrences = Collections.frequency(users, u);
                userFreq.put(u,occurrences);
            }

            return sortMap(userFreq);
        }

        @Override
        protected void onPostExecute(Map<String,Integer> userFreq) {
            BarChart barchart = findViewById(R.id.barGraph);
            ArrayList<BarEntry> barEntries= new ArrayList<>();

            String[] users = new String[5];

            try {
                for (int i = 0; i < 5; i++) {
                    users[i] = (String) userFreq.keySet().toArray()[i];
                    barEntries.add(new BarEntry(i, userFreq.get(userFreq.keySet().toArray()[i])));
                }
            }catch(ArrayIndexOutOfBoundsException e){
                CharSequence text = "There is not enough data";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(StatisticsActivity.this, text, duration).show();
            }

            BarDataSet entriesDataSet = new BarDataSet(barEntries,"Users");
            entriesDataSet.setColors(ColorTemplate.PASTEL_COLORS);
            BarData bd = new BarData (entriesDataSet);
            barchart.setData(bd);

            XAxis xAxis = barchart.getXAxis();
            xAxis.setValueFormatter(new LabelValueFormatter(users));
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1);
            xAxis.setDrawGridLines(false);

        }

    }

    private class DbFavTwittsAsyncTask extends AsyncTask<Void , Void, Map<String,Integer>> {


        @Override
        protected  Map<String,Integer> doInBackground(Void...voids) {

            AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
            List<Twitt> favTwittsList = db.twittDao().getAll();
            Map<String,Integer> favTwittsMap = new LinkedHashMap<>();
            for (Twitt t: favTwittsList){
                favTwittsMap.put(t.getText(), t.getFcount());
            }
            return sortMap(favTwittsMap);
        }

        @Override
        protected void onPostExecute(Map<String,Integer> favTwitts) {
            TextView tv = findViewById(R.id.favTwitts);


                try {
                    for (int i = 0; i < 10; i++) {
                        String key = (String) favTwitts.keySet().toArray()[i];
                        int fcount = favTwitts.get(key);
                        tv.append(key + "\n" + "Number of likes: "+fcount+"\n");
                        tv.append("\n");
                    }
                }catch(ArrayIndexOutOfBoundsException e){

                }




        }

    }

    private class DbRetTwittsAsyncTask extends AsyncTask<Void , Void, Map<String,Integer>> {


        @Override
        protected  Map<String,Integer> doInBackground(Void...voids) {

            AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
            List<Twitt> favTwittsList = db.twittDao().getAll();
            Map<String,Integer> retTwittsMap = new LinkedHashMap<>();
            for (Twitt t: favTwittsList){
                retTwittsMap.put(t.getText(), t.getRcount());
            }
            return sortMap(retTwittsMap);
        }

        @Override
        protected void onPostExecute(Map<String,Integer> retTwitts) {
            TextView tv = findViewById(R.id.retTwitts);


            try {
                for (int i = 0; i < 10; i++) {
                    String key = (String) retTwitts.keySet().toArray()[i];
                    int fcount = retTwitts.get(key);
                    tv.append(key + "\n" + "Number of retwitts: "+fcount+"\n");
                    tv.append("\n");
                }
            }catch(ArrayIndexOutOfBoundsException e){

            }




        }

    }

    public class LabelValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public LabelValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            return mValues[(int)value];
        }
    }


}
