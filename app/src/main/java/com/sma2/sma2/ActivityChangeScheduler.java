package com.sma2.sma2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sma2.sma2.DataAccess.PatientDataService;

public class ActivityChangeScheduler extends AppCompatActivity implements View.OnClickListener {

    RadioGroup rg_exercise;
    SharedPreferences sharedPref;

    String daily;
    String full;
    String speech;
    String movement;
    String demo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_scheduler);
        findViewById(R.id.button_next_scheduler).setOnClickListener(this);
        rg_exercise = findViewById(R.id.scheduler_radio);

        daily=getResources().getString(R.string.daily_exercises);
        full=getResources().getString(R.string.full_exercises);
        speech=getResources().getString(R.string.speech_exercises);
        movement=getResources().getString(R.string.mov_exercises);
        demo=getResources().getString(R.string.demo_exercises);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_next_scheduler:
                String exercises = ((RadioButton) findViewById(rg_exercise.getCheckedRadioButtonId())).getText().toString();

                sharedPref =PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.apply();


                if (exercises.equals(daily)){
                    editor.putString("exercises", "daily");
                    editor.apply();
                }
                else if (exercises.equals(full)){
                    editor.putString("exercises", "full");
                    editor.apply();
                }
                else if (exercises.equals(speech)){
                    editor.putString("exercises", "speech");
                    editor.apply();
                }
                else if (exercises.equals(movement)){
                    editor.putString("exercises", "movement");
                    editor.apply();
                }
                else{
                    editor.putString("exercises", "demo");
                    editor.apply();
                }
                Intent intent_main =new Intent(this, MainActivityMenu.class);
                startActivity(intent_main);
                break;
        }
    }

}
