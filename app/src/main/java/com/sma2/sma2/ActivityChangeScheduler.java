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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_scheduler);
        findViewById(R.id.button_next_scheduler).setOnClickListener(this);
        rg_exercise = findViewById(R.id.scheduler_radio);
    }





    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_next_scheduler:
                String exercises = ((RadioButton) findViewById(rg_exercise.getCheckedRadioButtonId())).getText().toString();

                String daily=getResources().getString(R.string.daily_exercises);
                String full=getResources().getString(R.string.full_exercises);
                sharedPref =PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.apply();
                if (exercises.equals(daily)){
                    editor.putString("exercises", "daily");
                    editor.apply();
                }
                else{
                    editor.putString("exercises", "full");
                    editor.apply();
                }
                Intent intent_main =new Intent(this, MainActivityMenu.class);
                startActivity(intent_main);
                break;
        }
    }

}
