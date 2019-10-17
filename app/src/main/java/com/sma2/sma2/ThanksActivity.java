package com.sma2.sma2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;

import java.util.List;

public class ThanksActivity extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_menu);
        setContentView(R.layout.activity_thanks);
        TextView MessageTextView = findViewById(R.id.textView_total);
        setListeners();

        SignalDataService signalDataService =new SignalDataService(this);
        List<SignalDA> signals= signalDataService.getAllSignals();
        int NSignals=signals.size();

        String message=getString(R.string.completed)+" "+String.valueOf(NSignals)+" "+getString(R.string.btnExercisesText);
        MessageTextView.setText(message);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("New Area Total", true);
        editor.apply();

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
        Intent intent_results=new Intent(this, MainActivityMenu.class);
        startActivity(intent_results);
        //TODO: transitions to the dashboard screen

    }


}
