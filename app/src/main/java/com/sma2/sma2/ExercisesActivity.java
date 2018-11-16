package com.sma2.sma2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sma2.sma2.DataAccess.PatientDA;
import com.sma2.sma2.DataAccess.PatientDataService;
import com.sma2.sma2.ExerciseFragments.ExerciseFinished;
import com.sma2.sma2.ExerciseFragments.ExerciseFragment;
import com.sma2.sma2.ExerciseFragments.ExerciseInstructions;
import com.sma2.sma2.ExerciseFragments.SessionOverview;
import com.sma2.sma2.ExerciseLogic.ExerciseSessionManager;
import com.sma2.sma2.ExerciseLogic.ScheduledExercise;


public class ExercisesActivity extends AppCompatActivity implements ExerciseInstructions.OnStartClickedListener,
        SessionOverview.OnSessionControlListener, ExerciseFragment.OnExerciseCompletedListener, ExerciseFinished.OnExerciseActionListener {

    ExerciseSessionManager sessionManager;
    SessionOverview sessionOverview;
    ScheduledExercise nextExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new ExerciseSessionManager();
        sessionManager.updateExerciseListFromDB(this);
        nextExercise = null;

        sessionOverview = SessionOverview.newInstance(sessionManager.getScheduledExerciseList());

        setContentView(R.layout.activity_exercise);

        showSessionOverview();
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

    private void showSessionOverview() {
        showFragment(sessionOverview);
    }

    public void open_exercise() {
        ExerciseInstructions intro = ExerciseInstructions.newInstance(nextExercise.getExercise());
        showFragment(intro);
    }


    @Override
    public void onExerciseStartClicked() {
        if (nextExercise != null) {
            try {
                Class<? extends ExerciseFragment> fragmentClass = nextExercise.getExercise().getFragmentClass();
                ExerciseFragment fragment = fragmentClass.newInstance();
                showFragment(fragment.newInstance(nextExercise.getExercise()));
            } catch (Exception e) {
                throw new RuntimeException("Invalid Exercise Fragment provided");
            }
        }
    }

    @Override
    public void onNextExerciseClicked() {
        nextExercise = sessionManager.getNextExercise();
        open_exercise();
    }

    @Override
    public void onSessionFinishedClicked() {

        PatientDataService PatientData= new PatientDataService(this);
        Long NumPatients=PatientData.countPatients();
        if (NumPatients>0){
            PatientDA Patient=PatientData.getPatient();
            int sessions=Patient.getSessionCount();
            Patient.setSessionCount(sessions+1);
            PatientData.updatePatient(Patient);
        }
        Intent mIntent = new Intent(this, ThanksActivity.class);
        startActivity(mIntent);
    }

    @Override
    public void onExerciseFinished(String filePath) {
        nextExercise.complete(Uri.parse(filePath));
        showFragment(ExerciseFinished.newInstance(nextExercise));
    }

    @Override
    public void onRedoButtonClicked() {
        open_exercise();
    }

    @Override
    public void onDoneButtonClicked() {
        nextExercise.save(this);
        showSessionOverview();
    }
}
