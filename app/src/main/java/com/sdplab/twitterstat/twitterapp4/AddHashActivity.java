package com.sdplab.twitterstat.twitterapp4;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        TextView tv1 = findViewById(R.id.textView3);
        tv1.setText("");
        for(String s:hc.getTagList()){
            tv1.append(s+"\n");
        }
    }

}
