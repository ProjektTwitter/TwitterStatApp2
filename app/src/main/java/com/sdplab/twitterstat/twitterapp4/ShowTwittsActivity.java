package com.sdplab.twitterstat.twitterapp4;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class ShowTwittsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_twitts);
        DownloadingTwitts dt = new DownloadingTwitts();
        dt.execute();
    }

    private void runDowmlad(){
        ;
    }

    private class DownloadingTwitts extends AsyncTask<Void , Void, List<twitter4j.Status>>{


        @Override
        protected List<twitter4j.Status> doInBackground(Void...voids) {
            Twitter twitt = (Twitter) getIntent().getSerializableExtra("Twitter");
            List<twitter4j.Status> status = null;
            Query query;
            QueryResult result;
            for(String q: HashtagContainer.getInstance().getTagList()){
                query = new Query(q);
                try {
                    result = twitt.search(query);
                    status = result.getTweets();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            }
            return status;
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> status) {
            TextView tv1 = findViewById(R.id.textView2);

            for (twitter4j.Status s : status) {
                tv1.append(s.getUser().getName()+"\n");
                tv1.append(s.getText()+"\n");
                tv1.append("\n");
            }
        }

    }

}
