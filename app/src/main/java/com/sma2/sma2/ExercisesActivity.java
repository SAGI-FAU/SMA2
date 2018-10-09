package com.sma2.sma2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class ExercisesActivity extends AppCompatActivity implements ExerciseIntro.OnStartClickedListener, ExerciseStart.OnSessionStartListener {
    // TODO: Implement the connection to the Exersice Manager

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

    public void open_exercise() {
        // TODO: Remove example and start the real exercise
        ExerciseIntro intro = ExerciseIntro.newInstance(
                "Test",
                Uri.parse("test/path"),
                Uri.parse("test/path2"),
                3);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.exerciseContainer, intro);
        transaction.commit();
    }


    @Override
    public void onExerciseStartClicked(int scheduledExerciseId) {
        // TODO: Open the correct excercise Fragment
    }

    @Override
    public void onSessionStartClicked() {
        open_exercise();
    }
}
