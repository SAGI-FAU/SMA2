package com.sma2.sma2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sma2.sma2.ExerciseFragments.ExAudioRec;

public class ExercisesActivity extends AppCompatActivity implements ExerciseIntro.OnStartClickedListener, ExerciseStart.OnSessionStartListener, ExAudioRec.OnFragmentInteractionListener {
    ExerciseSessionManager sessionManager;
    ScheduledExercise nextExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new ExerciseSessionManager();
        sessionManager.createExerciseSession(); // TODO: Only for testing
        nextExercise = null;

        setContentView(R.layout.activity_exercise);

        showStartScreen();
    }

    @Override
    public void finish() {
        this.sessionManager = null;
        this.nextExercise = null;
        super.finish();
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.exerciseContainer, fragment);
        transaction.commit();
    }

    private void showStartScreen() {
        try {
            nextExercise = sessionManager.getNextExercise();
            ExerciseStart startScreen = new ExerciseStart();
            showFragment(startScreen);
        } catch (IndexOutOfBoundsException e) {
            // TODO: Show a end screen
            Intent mIntent = new Intent(this, MainActivity.class);
            startActivity(mIntent);
        }
    }

    public void open_exercise() {
        ExerciseIntro intro = ExerciseIntro.newInstance(
                nextExercise.getExercise().getName(),
                nextExercise.getExercise().getInstructionVideoPath(),
                nextExercise.getExercise().getInstructionTextPath());
        showFragment(intro);
    }


    @Override
    public void onExerciseStartClicked() {
        if (nextExercise != null) {
            try {
                showFragment(nextExercise.getExercise().getFragmentClass().newInstance());
            } catch (Exception e) {
                throw new RuntimeException("Invalid Exercise Fragment provided");
            }
        }
    }

    @Override
    public void onSessionStartClicked() {
        open_exercise();
    }

    @Override
    public void onExerciseFinished(String filePath) {
        nextExercise.complete(Uri.parse(filePath));
        showStartScreen();
    }
}
