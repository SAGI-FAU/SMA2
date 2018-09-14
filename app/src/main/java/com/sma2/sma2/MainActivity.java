package com.sma2.sma2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setListeners();
    }

    private void setListeners() {
        findViewById(R.id.btnProfile).setOnClickListener(this);
        findViewById(R.id.btnSettings).setOnClickListener(this);
        findViewById(R.id.btnExercises).setOnClickListener(this);
        findViewById(R.id.btnResults).setOnClickListener(this);
        findViewById(R.id.txtProfile).setOnClickListener(this);
        findViewById(R.id.txtSettings).setOnClickListener(this);
        findViewById(R.id.txtExercises).setOnClickListener(this);
        findViewById(R.id.txtResults).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnProfile:
            case R.id.txtProfile:
                //TODO: Implement transition to new activity
                break;
            case R.id.btnSettings:
            case R.id.txtSettings:
                //TODO: Implement transition to new activity
                break;
            case R.id.btnExercises:
            case R.id.txtExercises:
                //TODO: Implement transition to new activity
                break;
            case R.id.btnResults:
            case R.id.txtResults:
                //TODO: Implement transition to new activity
                break;
        }
    }
}
