package com.sdplab.twitterstat.twitterapp4;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;


public class AddHashActivity extends AppCompatActivity implements View.OnClickListener {

    private HashtagContainer hc;
    private EditText etOne;
    private LinearLayout ll;
    private ArrayList<Button> blist;
    private ArrayList<LinearLayout> llist;
    private ArrayList<TextView> tlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeChanger.setUpTheme(this);
        setContentView(R.layout.activity_add_hash);
        Button one = findViewById(R.id.button_add);
        one.setOnClickListener(this);
        etOne = findViewById(R.id.editText1);
        hc = HashtagContainer.getInstance();
        ll = findViewById(R.id.linearLayout);
        blist = new ArrayList<>();
        tlist = new ArrayList<>();
        llist = new ArrayList<>();
        refresh();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.button_add:
                hc.addTag(String.valueOf(etOne.getText()));
                Context context = getApplicationContext();
                CharSequence text = "Hashtag added successfuly";
                Log.i("Adding","Hashtag added");
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, text, duration).show();
                etOne.setText("#");
                refresh();
                break;

        }
    }


    private void refresh(){
        for(Button b :blist){
            b.setVisibility(View.GONE);
        }
        blist.clear();
        for(TextView t :tlist){
            t.setVisibility(View.GONE);
        }
        tlist.clear();
        for(LinearLayout l :llist){
            l.setVisibility(View.GONE);
        }
        llist.clear();
        for(int i = 0; i < hc.getTagList().size(); i++){
            LinearLayout l = new LinearLayout(this);
            l.setId(i);
            l.setWeightSum(1);
            l.setOrientation(LinearLayout.HORIZONTAL);
            l.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            TextView tv = new TextView(this);
            Button b = new Button(this);
            tv.setText(hc.getTagList().get(i));
            tv.setId(i);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,18f);
            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
            b.setText("X");
            b.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            b.setId(i);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   hc.getTagList().remove(v.getId());
                   refresh();
                }
            });
            blist.add(b);
            llist.add(l);
            tlist.add(tv);
            l.addView(tv);
            l.addView(b);
            ll.addView(l);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(SettingsFragment.themeChanged)

            recreate();
    }

}
