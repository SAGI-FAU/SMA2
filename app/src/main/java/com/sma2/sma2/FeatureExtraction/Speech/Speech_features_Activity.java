package com.sma2.sma2.FeatureExtraction.Speech;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sma2.sma2.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.sma2.sma2.RadarFigureManager;
import com.sma2.sma2.ResultsActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Speech_features_Activity extends AppCompatActivity implements View.OnClickListener {
    private Button bBack;

    private RadarFigureManager RadarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_features);
        bBack=findViewById(R.id.button_back_result);

        SetListeners();

        RadarManager=new RadarFigureManager(this);
        // Radar chart
        RadarChart radarchart= findViewById(R.id.chart2);


        float[] data1={(float) 10,(float) 10,(float) 10,(float) 10, (float) 10}; // Patient
        float[] data2={(float) 80,(float) 80,(float) 80,(float) 80, (float) 80}; // Healthy

        String Label_1 = getResources().getString(R.string.pronunciation);
        String Label_2 = getResources().getString(R.string.stability);
        String Label_3 = getResources().getString(R.string.rate);
        String Label_4 = getResources().getString(R.string.intonation);
        String Label_5 = getResources().getString(R.string.intelligibility);
        String[] labels={Label_1, Label_2, Label_3, Label_4, Label_5};


        RadarManager.PlotRadar(radarchart, data1, data2, labels);



    }

    private void SetListeners(){
        bBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_back_result:
                onButtonBack();
                break;
        }
    }

    private void onButtonBack(){
        Intent i =new Intent(Speech_features_Activity.this, ResultsActivity.class);
        startActivity(i);
    }






}
