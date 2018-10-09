package com.sma2.sma2;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ExercisesActivity extends AppCompatActivity implements ExerciseIntro.OnStartClickedListener, ExerciseStart.OnSessionStartListener {


    Intent intent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exercise);

        ExerciseStart startScreen = new ExerciseStart();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.exerciseContainer, startScreen);
        transaction.commit();
    }

    public void open_exercise(){
        ExerciseIntro intro = ExerciseIntro.newInstance("Test", Uri.parse("test/path"), Uri.parse("test/path2"));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.exerciseContainer, intro);
        transaction.commit();
    }


    @Override
    public void onExerciseStartClicked() {
        open_exercise();
    }

    @Override
    public void onSessionStartClicked() {
        open_exercise();
    }
}
