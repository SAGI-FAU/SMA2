package com.sma2.apkinson;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sma2.apkinson.DataAccess.Exercise;
import com.sma2.apkinson.DataAccess.PatientDA;
import com.sma2.apkinson.DataAccess.PatientDataService;
import com.sma2.apkinson.DataAccess.ScheduledExerciseDataService;
import com.sma2.apkinson.ExerciseFragments.ExerciseFinished;
import com.sma2.apkinson.ExerciseFragments.ExerciseFragment;
import com.sma2.apkinson.ExerciseFragments.ExerciseInstructions;
import com.sma2.apkinson.ExerciseFragments.SessionOverview;
import com.sma2.apkinson.DataAccess.ExerciseSessionManager;
import com.sma2.apkinson.DataAccess.ScheduledExercise;

import java.io.Serializable;


public class ExercisesActivity extends AppCompatActivity implements ExerciseInstructions.OnStartClickedListener,
        SessionOverview.OnSessionControlListener, ExerciseFragment.OnExerciseCompletedListener, ExerciseFinished.OnExerciseActionListener, Serializable {

    ExerciseSessionManager sessionManager;
    SessionOverview sessionOverview;
    ScheduledExercise nextExercise;
    boolean full_session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Keep the screen on during any exercise. Otherwise background recordings will stop
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sessionManager = new ExerciseSessionManager();
        sessionManager.updateExerciseListFromDB(this);


        Bundle bundle= getIntent().getExtras();

        setContentView(R.layout.activity_exercise);
        if (bundle==null){
            full_session=true;
        }
        else{
            full_session=false;
        }

        if (full_session){
            nextExercise = null;
            sessionOverview = SessionOverview.newInstance(sessionManager.getScheduledExerciseList());
            showSessionOverview();
        }
        else{
            Exercise ex=getIntent().getExtras().getParcelable("Exercise");
            nextExercise=new ScheduledExercise(ex, 0);
            ScheduledExerciseDataService scheduledExerciseDataService = new ScheduledExerciseDataService(this);
            scheduledExerciseDataService.saveScheduledExercise(nextExercise);
            open_exercise2(ex);
        }

        FloatingActionButton fab = findViewById(R.id.fab_settings);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_settings();
            }
        });


    }

    public void open_settings() {
        //TODO: Implement as Dialog
        Intent intent_settings = new Intent(ExercisesActivity.this, SettingsActivity.class);
        startActivity(intent_settings);
    }

    @Override
    public void finish() {
        this.sessionManager = null;
        this.nextExercise = null;
        super.finish();
    }

    public void showFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.exerciseContainer, fragment);
        transaction.commit();
    }

    private void showSessionOverview() {
        showFragment(sessionOverview);
    }

    public void open_exercise() {
        Exercise ex=nextExercise.getExercise();
        ExerciseInstructions intro = ExerciseInstructions.newInstance(ex);
        showFragment(intro);
    }

    public void open_exercise2(Exercise exercise) {
        ExerciseInstructions intro = ExerciseInstructions.newInstance(exercise);
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

        if (full_session){
            if (sessionManager.isSessionFinished()){
                showSessionOverview();
            }
            else{
                nextExercise = sessionManager.getNextExercise();
                open_exercise();
                //showSessionOverview();
            }
        }
        else{
            onSessionFinishedClicked();
        }

    }
}
