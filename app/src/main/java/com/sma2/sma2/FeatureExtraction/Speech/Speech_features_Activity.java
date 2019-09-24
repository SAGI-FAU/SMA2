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
import com.sma2.sma2.ResultsActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Speech_features_Activity extends AppCompatActivity implements View.OnClickListener {
    private Button bBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_features);
        bBack=findViewById(R.id.button_back_result);

        SetListeners();

        // Radar chart
        RadarChart radarchart= findViewById(R.id.chart2);
        radarchart.getDescription().setEnabled(false);
        radarchart.animateXY(5000, 5000, Easing.EaseInOutQuad);

        float[] datos1={(float) 10,(float) 10,(float) 10,(float) 10, (float) 10}; // Patient
        float[] datos2={(float) 80,(float) 80,(float) 80,(float) 80, (float) 80}; // Healthy

        RadarData radardata=setdata(datos1,datos2);

        String Label_1 = getResources().getString(R.string.pronunciation);
        String Label_2 = getResources().getString(R.string.stability);
        String Label_3 = getResources().getString(R.string.rate);
        String Label_4 = getResources().getString(R.string.intonation);
        String Label_5 = getResources().getString(R.string.intelligibility);
        String[] labels={Label_1, Label_2, Label_3, Label_4, Label_5};

        XAxis xAxis=radarchart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setLabelRotationAngle(90f);

        YAxis yAxis = radarchart.getYAxis();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);

        Legend l = radarchart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.BLACK);
        l.setTextSize(20f);

        radarchart.setExtraOffsets(0,-400,0,-400);
        //radarchart.setBackgroundColor(Color.WHITE);
        radarchart.setScaleY(1f);
        radarchart.setScaleX(1f);
        radarchart.setData(radardata);
        radarchart.invalidate(); // refresh

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

    private RadarData setdata(float[] datos1, float[] datos2) {
        int cnt = datos1.length;
        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            //float val1 = (float) (Math.random() * mul) + min;
            entries1.add(new RadarEntry(datos1[i]));

            //float val2 = (float) (Math.random() * mul) + min;
            entries2.add(new RadarEntry(datos2[i]));
        }

        String Label_Patient = getResources().getString(R.string.patient);
        String Label_Control = getResources().getString(R.string.control);


        RadarDataSet set1 = new RadarDataSet(entries1, Label_Patient);
        set1.setColor(Color.rgb(255, 185, 0));
        set1.setFillColor(Color.rgb(255, 185, 0));
        set1.setDrawFilled(true);
        set1.setFillAlpha(200);
        set1.setLineWidth(2f);
        set1.setValueTextColor(Color.rgb(255, 185, 0));
        set1.setValueTextSize(15f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, Label_Control);
        set2.setColor(Color.rgb(0, 200, 200));
        set2.setFillColor(Color.rgb(0, 200, 200));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);
        set2.setValueTextColor(Color.rgb(0, 200, 200));
        set2.setValueTextSize(15f);

        RadarData dataradar= new RadarData();

        dataradar.addDataSet(set1);
        dataradar.addDataSet(set2);
        return dataradar;
    }




}
