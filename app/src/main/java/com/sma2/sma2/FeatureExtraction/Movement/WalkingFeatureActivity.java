package com.sma2.sma2.FeatureExtraction.Movement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;
import com.sma2.sma2.FeatureExtraction.GetExercises;
import com.sma2.sma2.FeatureExtraction.GraphManager;
import com.sma2.sma2.FeatureExtraction.Speech.Speech_features_Activity;
import com.sma2.sma2.FeatureExtraction.Speech.features.RadarFeatures;
import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;
import com.sma2.sma2.RadarFigureManager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.RadarChart;
import com.sma2.sma2.ResultsActivity;


public class WalkingFeatureActivity extends AppCompatActivity implements View.OnClickListener {

    Button bBack;
    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";
    String path_movement = null;
    List<String> path_movement_all = new ArrayList<>();
    TextView tWalking;
    //private double maxArea = 23776; //Five features
    private double maxArea = 2598.76; //Six features

    int screenWidth, screenHeight;

    private float tremorValue = 0;
    private float regularityValue = 0;
    private float freezeValue = 0;
    private float postureValue = 0;
    private float nStridesValue = 0;
    private float tStrideValues = 0;

    TextView tVelocity;
    private ProgressBar progressBarMov;
    private ImageView iEmojin;
    private TextView tmessage_Mov, tmessage_Mov_perc;
    private RadarFigureManager RadarManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking_feature);
        bBack = findViewById(R.id.button_back_result_mov);
        progressBarMov = findViewById(R.id.bar_mov);
        iEmojin = findViewById(R.id.iEmojin_Mov);
        tmessage_Mov = findViewById(R.id.tmessage_mov);
        tmessage_Mov_perc = findViewById(R.id.tmessage_mov_perc);
        getDisplayDimensions();
        SetListeners();
        RadarManager = new RadarFigureManager(this);
        // Radar chart
        RadarChart radarchart = findViewById(R.id.chart2);

        ArrayList<Float> area_perf = new ArrayList<>();

        try {
            area_perf = RadarFeatures.get_feat_perf("area movement");
        } catch (IOException ie) {
            ie.printStackTrace();
            area_perf.add(0f);
        }



        //Feature Values

        freezeValue=FreezeIndex();

        float[] data1 = {tremorValue, regularityValue, freezeValue, postureValue, nStridesValue, tStrideValues}; // Patient


        float[] data2 = {(float) 50, (float) 50, (float) 50, (float) 50, (float) 50, (float) 50}; // Healthy

        String Label_1 = getResources().getString(R.string.tremor);
        String Label_2 = getResources().getString(R.string.regularity);
        String Label_3 = getResources().getString(R.string.freezeIndex);
        String Label_4 = getResources().getString(R.string.posture);
        String Label_5 = getResources().getString(R.string.nStrides);
        String Label_6 = getResources().getString(R.string.tStrides);

        String[] labels = {Label_1, Label_2, Label_3, Label_4, Label_5, Label_6};


        RadarManager.PlotRadar(radarchart, data1, data2, labels);


        double area = RadarManager.get_area_chart(data1);

        int area_progress = (int) (area * 100 / maxArea);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean new_area_speech = sharedPref.getBoolean("New Area Mov", false);

        if (new_area_speech) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("New Area Movement", false);
            editor.apply();
            try {
                RadarFeatures.export_speech_feature("AreaMov", area_progress, "area mov");
            } catch (Exception e) {
                Toast.makeText(this, R.string.jitter_failed, Toast.LENGTH_SHORT).show();

            }

        }


        ConstraintLayout.LayoutParams params_line = (ConstraintLayout.LayoutParams) iEmojin.getLayoutParams();


        int xRandomBar = (int) (0.01 * area_progress * screenWidth - 45);



        params_line.setMarginStart(xRandomBar); // The indicator bar position
        params_line.leftMargin = xRandomBar;
        params_line.setMarginStart(xRandomBar);
        iEmojin.setLayoutParams(params_line);


        progressBarMov.setProgress(area_progress);
        String msgp = "Performance: " + String.valueOf(area_progress) + "%";
        tmessage_Mov_perc.setText(msgp);
        if (area_progress >= 66) {
            iEmojin.setImageResource(R.drawable.happy_emojin);
            tmessage_Mov.setText(R.string.Positive_message);
        } else if (area_progress >= 33) {
            iEmojin.setImageResource(R.drawable.medium_emojin);
            tmessage_Mov.setText(R.string.Medium_message);
        } else {
            iEmojin.setImageResource(R.drawable.sad_emoji);
            tmessage_Mov.setText(R.string.Negative_message);
        }
        GraphManager graphManager = new GraphManager(this);
        String Title = getResources().getString(R.string.speech_performance);
        String Ylabel = getResources().getString(R.string.percentage);
        String Xlabel = getResources().getString(R.string.session);
        GraphView graph = findViewById(R.id.plotlineMovement);

        ArrayList<Integer> xl = new ArrayList<>();
        for (int i = 0; i < area_perf.size(); i++) {
            xl.add(i + 1);
        }
        graphManager.LineGraph(graph, xl, area_perf, 105, xl.size(), Title, Xlabel, Ylabel);


    }


    private void getDisplayDimensions() {
        Display display = this.getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    private void SetListeners() {
        bBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_back_result:
                onButtonBack();
                break;
        }
    }

    private void onButtonBack() {
        Intent i = new Intent(WalkingFeatureActivity.this, ResultsActivity.class);
        startActivity(i);
    }


    //Temporal Functions
    private float FreezeIndex() {

        SignalDataService signalDataService = new SignalDataService(this);
        CSVFileReader FileReader = new CSVFileReader(this);
        MovementProcessing movementProcessing = new MovementProcessing();
        DecimalFormat df = new DecimalFormat("#.0");

        int IDEx = 31;
        GetExercises GetEx = new GetExercises(this);
        String name = GetEx.getNameExercise(IDEx);
        List<SignalDA> Signals = signalDataService.getSignalsbyname(name);
        if (Signals.size() > 0) {
            path_movement = PATH + Signals.get(Signals.size() - 1).getSignalPath();

            if (Signals.size() > 4) {
                for (int i = Signals.size() - 4; i < Signals.size(); i++) {
                    path_movement_all.add(PATH + Signals.get(i).getSignalPath());
                }
            } else {
                for (int i = 0; i < Signals.size(); i++) {
                    path_movement_all.add(PATH + Signals.get(i).getSignalPath());
                }
            }

        }


        if (path_movement == null) {
            return (float) 0;
        }


    else {

            CSVFileReader.Signal GaitSignalaX = FileReader.ReadMovementSignal(path_movement, "aX [m/s^2]");
            CSVFileReader.Signal GaitSignalaY = FileReader.ReadMovementSignal(path_movement, "aY [m/s^2]");
            CSVFileReader.Signal GaitSignalaZ = FileReader.ReadMovementSignal(path_movement, "aZ [m/s^2]");

            List<Double> AccXn, AccYn, AccZn, AccR;
            AccXn = movementProcessing.RemoveGravity(GaitSignalaX.Signal);
            AccYn = movementProcessing.RemoveGravity(GaitSignalaY.Signal);
            AccZn = movementProcessing.RemoveGravity(GaitSignalaZ.Signal);
            AccR = movementProcessing.getAccR(AccXn, AccYn, AccZn);
            List<Double> oldTime = GaitSignalaX.TimeStamp;


            CSVFileReader.Signal timeSteps = FileReader.ReadMovementSignal(path_movement, "Timestamp [ns]");


            List<Double> newAccR = new ArrayList<>();
            List<Double> newOldTime = new ArrayList<>();


            newAccR = movementProcessing.removeInitGait(AccR, 100, 0.2, 0.02);
            for (int i = oldTime.size() - newAccR.size(); i < oldTime.size(); i++) {

                newOldTime.add(oldTime.get(i));


            }


            float fIndex = movementProcessing.freezeIndex(newAccR, newOldTime, (int) 100);


            //Computing the percentage based on controls
            // Reference (100%) = 0.07 or less
            //It must be replace to real values, it was computed based one woman and one men

            float perc_fidex=(float) (200/(1+Math.exp(10*(fIndex-0.07))));




        return  perc_fidex;


    }
}

}






