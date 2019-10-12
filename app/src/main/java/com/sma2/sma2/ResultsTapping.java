package com.sma2.sma2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.sma2.sma2.FeatureExtraction.GraphManager;
import com.sma2.sma2.FeatureExtraction.Speech.features.RadarFeatures;
import com.sma2.sma2.FeatureExtraction.Tapping.FeatureTapping;

import com.github.mikephil.charting.charts.RadarChart;

import java.io.IOException;
import java.util.ArrayList;


public class ResultsTapping extends AppCompatActivity  implements View.OnClickListener {
    Button bHistory;
    private ImageButton bHelp;
    private ProgressBar progressBarTapping;
    private ImageView iEmojin;
    private TextView tmessage_tapping, tmessage_tapping_perc;
    int screenWidth, screenHeight;

    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";

    //"/storage/emulated/0/AppSpeechData/ACC/Tapping_example.csv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // general objects
        FeatureTapping feature= new FeatureTapping(this);
        setContentView(R.layout.activity_tapping_feature_);
        bHelp=findViewById(R.id.button_help);
        bHelp.bringToFront();
        bHistory=findViewById(R.id.button_history);
        SetListeners();

        progressBarTapping=findViewById(R.id.bar_tapping);
        iEmojin=findViewById(R.id.iEmojin_tapping);
        tmessage_tapping=findViewById(R.id.tmessage_tapping);
        tmessage_tapping_perc=findViewById(R.id.tmessage_tapping_perc);
        getDisplayDimensions();

        RadarFigureManager RadarManager = new RadarFigureManager(this);
        // Radar chart
        RadarChart radarchart= findViewById(R.id.chart_tapping);

        float[] data1 =feature.totalfeatures(PATH);
        float[] data2={100f,100f,100f,100f};

        String Label_3 = getResources().getString(R.string.tapPosition);
        String Label_1 = getResources().getString(R.string.tapHits);
        String Label_4 = getResources().getString(R.string.barHits);
        String Label_2 = getResources().getString(R.string.tapVelocity);
        String[] labels={Label_1, Label_2, Label_3, Label_4};

        RadarManager.PlotRadar(radarchart, data1, data2, labels);

        double area=RadarManager.get_area_chart(data1);
        double maxArea=RadarManager.get_area_chart(data2);
        int area_progress=(int)(area*100/maxArea);

        SharedPreferences sharedPref =PreferenceManager.getDefaultSharedPreferences(this);
        boolean new_area_tapping=sharedPref.getBoolean("New Area Tapping", false);


        if (new_area_tapping){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("New Area Tapping", false);
            editor.apply();
            try {
                RadarFeatures.export_speech_feature("area tapping", area_progress, "area tapping");
            }catch (Exception e) {
                Toast.makeText(this,R.string.tapping_failed,Toast.LENGTH_SHORT).show();

            }

        }
        ArrayList<Float> area_perf=new ArrayList<>();
        try {
            area_perf=RadarFeatures.get_feat_perf("area tapping");
        }
        catch(IOException ie) {
            ie.printStackTrace();
            area_perf.add(0f);
        }

        LinearLayout.LayoutParams params_line= (LinearLayout.LayoutParams)  iEmojin.getLayoutParams();
        int xRandomBar= (int)(0.01*area_progress*screenWidth-45);

        params_line.setMarginStart(xRandomBar); // The indicator bar position
        params_line.leftMargin=xRandomBar;
        params_line.setMarginStart(xRandomBar);
        iEmojin.setLayoutParams(params_line);

        progressBarTapping.setProgress(area_progress);
        String msgp=String.valueOf(area_progress)+"%";
        tmessage_tapping_perc.setText(msgp);
        if (area_progress >=66) {
            iEmojin.setImageResource(R.drawable.happy_emojin);
            tmessage_tapping.setText(R.string.Positive_message);
        }
        else if (area_progress>=33){
            iEmojin.setImageResource(R.drawable.medium_emojin);
            tmessage_tapping.setText(R.string.Medium_message);
        }
        else{
            iEmojin.setImageResource(R.drawable.sad_emoji);
            tmessage_tapping.setText(R.string.Negative_message);
        }




    }

    private void SetListeners(){
        bHistory.setOnClickListener(this);
        bHelp.setOnClickListener(this);
    }


    private void getDisplayDimensions() {
        Display display = this.getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_history:
                //TODO: button history action
                break;
            case R.id.button_help:
                onButtonHelp();
                break;
        }
    }

    


    private void onButtonHelp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String Title = getResources().getString(R.string.interpre);
        builder.setTitle(Title);

        String Text = getResources().getString(R.string.TappingHelp);
        builder.setMessage(Text);

        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() { // define the 'Cancel' button
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
