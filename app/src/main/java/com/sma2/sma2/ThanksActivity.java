package com.sma2.sma2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ThanksActivity extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_menu);
        setContentView(R.layout.activity_thanks);
        setListeners();

    }

    private void setListeners() {
        findViewById(R.id.button_results).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_results:
                open_results();
                break;
        }
    }


    public void open_results(){
        Intent intent_results=new Intent(this, MainActivity.class);
        startActivity(intent_results);
        //TODO: transitions to the dashboard screen

    }


}
