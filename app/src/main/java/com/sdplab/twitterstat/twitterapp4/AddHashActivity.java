package com.sdplab.twitterstat.twitterapp4;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class AddHashActivity extends AppCompatActivity implements View.OnClickListener {

    private HashtagContainer hc;
    private EditText etOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hash);
        Button one = findViewById(R.id.button_add);
        one.setOnClickListener(this);

        etOne = findViewById(R.id.editText1);
        hc = HashtagContainer.getInstance();
        refresh();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.button_add:
                hc.addTag(String.valueOf(etOne.getText()));
                Context context = getApplicationContext();
                CharSequence text = "Hashtag added successfuly";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, text, duration).show();
                etOne.setText("");
                refresh();
                break;

        }
    }

    private void refresh(){
        LinearLayout ll = findViewById(R.id.linearLayout);
        ArrayList<Button> blist = new ArrayList<>();
        for(int i = 0; i < hc.getTagList().size(); i++){
            for(Button b :blist){
                b.setVisibility(View.GONE);
            }
            LinearLayout l = new LinearLayout(this);
            l.setId(i);
            l.setOrientation(LinearLayout.HORIZONTAL);
            l.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            TextView tv = new TextView(this);
            Button b = new Button(this);
            tv.setText(hc.getTagList().get(i));
            tv.setId(i);
            tv.setGravity(LinearLayout.FOCUS_LEFT);
            b.setText("X");
            b.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            b.setGravity(LinearLayout.FOCUS_RIGHT);
            b.setId(i);
            blist.add(b);
            l.addView(tv);
            l.addView(b);
            ll.addView(l);
        }
    }

}
