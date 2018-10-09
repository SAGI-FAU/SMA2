package com.sma2.sma2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sma2.sma2.ExerciseFragments.ExSustainedVowel;

public class ExercisesActivity extends AppCompatActivity implements ExerciseIntro.OnStartClickedListener, ExerciseStart.OnSessionStartListener, ExSustainedVowel.OnFragmentInteractionListener {
    ExerciseSessionManager sessionManager;
    ScheduledExercise nextExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new ExerciseSessionManager();
        nextExercise = null;

        setContentView(R.layout.activity_exercise);

        ExerciseStart startScreen = new ExerciseStart();
        showFragment(startScreen);
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.exerciseContainer, fragment);
        transaction.commit();
    }

    public void open_exercise() {
        nextExercise = sessionManager.getNextExercise();
        ExerciseIntro intro = ExerciseIntro.newInstance(
                nextExercise.getExercise().getName(),
                nextExercise.getExercise().getInstructionVideoPath(),
                nextExercise.getExercise().getInstructionTextPath(),
                nextExercise.getSessionId());

        showFragment(intro);
    }


    @Override
    public void onExerciseStartClicked(int scheduledExerciseId) {
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
        //do something with file here
    }
}
