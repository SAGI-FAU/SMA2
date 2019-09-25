package com.sma2.sma2.FeatureExtraction.Tapping;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;
import com.sma2.sma2.RadarFigureManager;

import com.github.mikephil.charting.charts.RadarChart;


public class Tapping_feature_Activity extends AppCompatActivity  implements View.OnClickListener {
    Button bBack;
    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";

    //"/storage/emulated/0/AppSpeechData/ACC/Tapping_example.csv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // general objects
        FeatureTapping feature= new FeatureTapping(this);
        setContentView(R.layout.activity_tapping_feature_);
        bBack=findViewById(R.id.button_back4);
        bBack.setOnClickListener(this);

        RadarFigureManager RadarManager = new RadarFigureManager(this);
        // Radar chart
        RadarChart radarchart= findViewById(R.id.chartap);

        float[] data1 =feature.totalfeatures(PATH);
        float[] data2={100f,100f,100f,100f};

        String Label_3 = getResources().getString(R.string.tapPosition);
        String Label_1 = getResources().getString(R.string.tapHits);
        String Label_4 = getResources().getString(R.string.barHits);
        String Label_2 = getResources().getString(R.string.tapVelocity);
        String[] labels={Label_1, Label_2, Label_3, Label_4};

        RadarManager.PlotRadar(radarchart, data1, data2, labels);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_back4:
                onButtonBack();
                break;
        }
    }

    private void onButtonBack(){
        Intent i =new Intent(Tapping_feature_Activity.this, MainActivityMenu.class);
        startActivity(i);

    }

}
