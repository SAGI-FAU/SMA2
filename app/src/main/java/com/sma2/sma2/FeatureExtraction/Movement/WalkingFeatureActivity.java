package com.sma2.sma2.FeatureExtraction.Movement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;
import com.sma2.sma2.FeatureExtraction.GetExercises;
import com.sma2.sma2.FeatureExtraction.GraphManager;
import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;
import com.sma2.sma2.RadarFigureManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;

import android.graphics.Color;

public class WalkingFeatureActivity extends AppCompatActivity implements View.OnClickListener {

    Button bBack;
    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";
    String path_movement = null;
    List<String> path_movement_all = new ArrayList<>();
    TextView tWalking;
    TextView tVelocity;
    private RadarFigureManager RadarManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking_feature);
        bBack = findViewById(R.id.button_back6);
        bBack.setOnClickListener(this);
        tWalking = findViewById(R.id.tTremorWalking);
        tVelocity = findViewById(R.id.tVelocityWalking);
        SignalDataService signalDataService = new SignalDataService(this);
        CSVFileReader FileReader = new CSVFileReader(this);
        MovementProcessing MovementProcessor = new MovementProcessing();
        DecimalFormat df = new DecimalFormat("#.0");

        int IDEx = 31;   // Gait 4x10
        GetExercises GetEx = new GetExercises(this);

        String name = GetEx.getNameExercise(IDEx);
        double tremor = 0.0d;
        double steps = 0.0d;
        double velocity = 0.0d;
        double movTremor = 0;
        float[] pitch;
        double power = 0.0;

        List<SignalDA> Signals = signalDataService.getSignalsbyname(name);
        if (Signals.size() > 0){
            path_movement = PATH + Signals.get(Signals.size()-1).getSignalPath();

            if (Signals.size() > 4){
                for (int i = Signals.size()- 4; i < Signals.size(); i++){
                    path_movement_all.add(PATH+Signals.get(i).getSignalPath());
                }
            }
            else{
                for (int i = 0; i < Signals.size(); i++){
                    path_movement_all.add(PATH+Signals.get(i).getSignalPath());
                }
            }

        }

        ArrayList<Float> accX = new ArrayList<>();
        ArrayList<Float> accY = new ArrayList<>();
        ArrayList<Float> timeX = new ArrayList<>();
        ArrayList<Float> timeY = new ArrayList<>();

        if(path_movement == null){
            tWalking.setText(R.string.Empty);
            tVelocity.setText(R.string.Empty);
        }
        else {
            CSVFileReader.Signal GaitSignalaX = FileReader.ReadMovementSignal(path_movement, "aX [m/s^2]");
            CSVFileReader.Signal GaitSignalaY = FileReader.ReadMovementSignal(path_movement, "aY [m/s^2]");
            CSVFileReader.Signal GaitSignalaZ = FileReader.ReadMovementSignal(path_movement, "aZ [m/s^2]");

            accX = MovementProcessor.getAccX(GaitSignalaX.Signal);

            steps = MovementProcessor.getSteps(accX);
            velocity = MovementProcessor.getSteps(accX);
            tWalking.setText(String.valueOf(df.format(steps)));
            tVelocity.setText(String.valueOf(df.format(velocity)));
            System.out.println(GaitSignalaX.Signal.size());
        }

        RadarManager = new RadarFigureManager(this);
        // Radar chart
        RadarChart radarchart= findViewById(R.id.chart2);

        float[] data1={(float) 10,(float) 10,(float) 10,(float) 10}; // Patient
        float[] data2={(float) 80,(float) 80,(float) 80,(float) 80}; // Healthy

        String Label_1 = getResources().getString(R.string.steps);
        String Label_2 = getResources().getString(R.string.stability);
        String Label_3 = getResources().getString(R.string.velocity);
        String Label_4 = getResources().getString(R.string.tremor);
        String[] labels={Label_1, Label_2, Label_3, Label_4};

        RadarManager.PlotRadar(radarchart, data1, data2, labels);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_back6:
                onButtonBack();
                break;
        }
    }

    private void onButtonBack(){
        Intent i =new Intent(WalkingFeatureActivity.this, MainActivityMenu.class);
        startActivity(i);
    }

}