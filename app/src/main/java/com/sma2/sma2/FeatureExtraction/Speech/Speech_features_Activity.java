package com.sma2.sma2.FeatureExtraction.Speech;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.sma2.sma2.FeatureExtraction.GraphManager;
import com.sma2.sma2.FeatureExtraction.Speech.features.RadarFeatures;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Speech_features_Activity extends AppCompatActivity implements View.OnClickListener {
    private Button bBack;

    private RadarFigureManager RadarManager;
    private double maxArea=23776;
    private ProgressBar progressBarSpeech;
    private ImageView iEmojin;
    private TextView tmessage_speech, tmessage_speech_perc;
    int screenWidth, screenHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_features);
        bBack=findViewById(R.id.button_back_result);
        progressBarSpeech=findViewById(R.id.bar_speech);
        iEmojin=findViewById(R.id.iEmojin_speech);
        tmessage_speech=findViewById(R.id.tmessage_speech);
        tmessage_speech_perc=findViewById(R.id.tmessage_speech_perc);
        getDisplayDimensions();
        SetListeners();

        RadarManager = new RadarFigureManager(this);
        // Radar chart
        RadarChart radarchart= findViewById(R.id.chart2);
        ArrayList<Float> jitter_perf=new ArrayList<>();
        ArrayList<Float> vrate_perf=new ArrayList<>();
        ArrayList<Float> inton_perf=new ArrayList<>();
        ArrayList<Float> area_perf=new ArrayList<>();
        try {
            jitter_perf=RadarFeatures.get_feat_perf("Jitter");
        }
        catch(IOException ie) {
            jitter_perf.add(0f);
            ie.printStackTrace();
        }
        try {
            vrate_perf=RadarFeatures.get_feat_perf("VRate");
        }
        catch(IOException ie) {
            vrate_perf.add(0f);
            ie.printStackTrace();
        }
        try {
            inton_perf=RadarFeatures.get_feat_perf("Intonation");
        }
        catch(IOException ie) {
            ie.printStackTrace();
            inton_perf.add(0f);
        }

        try {
            area_perf=RadarFeatures.get_feat_perf("area speech");
        }
        catch(IOException ie) {
            ie.printStackTrace();
            area_perf.add(0f);
        }


        float[] data1={(float) 60, jitter_perf.get(jitter_perf.size()-1),vrate_perf.get(vrate_perf.size()-1),inton_perf.get(inton_perf.size()-1), (float) 90}; // Patient
        float[] data2={(float) 86,(float) 98,(float) 73.7,(float) 55.4, (float) 100}; // Healthy

        String Label_1 = getResources().getString(R.string.pronunciation);
        String Label_2 = getResources().getString(R.string.stability);
        String Label_3 = getResources().getString(R.string.rate);
        String Label_4 = getResources().getString(R.string.intonation);
        String Label_5 = getResources().getString(R.string.intelligibility);
        String[] labels={Label_1, Label_2, Label_3, Label_4, Label_5};


        RadarManager.PlotRadar(radarchart, data1, data2, labels);


        double area=RadarManager.get_area_chart(data1);
        int area_progress=(int)(area*100/maxArea);

        SharedPreferences sharedPref =PreferenceManager.getDefaultSharedPreferences(this);
        boolean new_area_speech=sharedPref.getBoolean("New Area Speech", false);

        if (new_area_speech){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("New Area Speech", false);
            editor.apply();
            try {
                RadarFeatures.export_speech_feature("AreaSpeech", area_progress, "area speech");
            }catch (Exception e) {
                Toast.makeText(this,R.string.jitter_failed,Toast.LENGTH_SHORT).show();

            }

        }



        ConstraintLayout.LayoutParams params_line= (ConstraintLayout.LayoutParams)  iEmojin.getLayoutParams();
        int xRandomBar= (int)(0.01*area_progress*screenWidth-45);


        params_line.setMarginStart(xRandomBar); // The indicator bar position
        params_line.leftMargin=xRandomBar;
        params_line.setMarginStart(xRandomBar);
        iEmojin.setLayoutParams(params_line);



        progressBarSpeech.setProgress(area_progress);
        String msgp="Performance: "+String.valueOf(area_progress)+"%";
        tmessage_speech_perc.setText(msgp);
        if (area_progress >=66) {
            iEmojin.setImageResource(R.drawable.happy_emojin);
            tmessage_speech.setText(R.string.Positive_message);
        }
        else if (area_progress>=33){
            iEmojin.setImageResource(R.drawable.medium_emojin);
            tmessage_speech.setText(R.string.Medium_message);
        }
        else{
            iEmojin.setImageResource(R.drawable.sad_emoji);
            tmessage_speech.setText(R.string.Negative_message);
        }
        GraphManager graphManager=new GraphManager(this);
        String Title=getResources().getString(R.string.speech_performance);
        String Ylabel=getResources().getString(R.string.percentage);
        String Xlabel=getResources().getString(R.string.session);
        GraphView graph =findViewById(R.id.plotlineSpeech);

        ArrayList<Integer> xl= new ArrayList<>();
        for (int i=0;i<area_perf.size();i++){
            xl.add(i+1);
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
