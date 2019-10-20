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
import com.sma2.sma2.DataAccess.FeatureDA;
import com.sma2.sma2.DataAccess.FeatureDataService;
import com.sma2.sma2.FeatureExtraction.GraphManager;
import com.sma2.sma2.FeatureExtraction.Speech.features.RadarFeatures;
import com.sma2.sma2.FeatureExtraction.Tapping.FeatureTapping;

import com.github.mikephil.charting.charts.RadarChart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ResultsTapping extends AppCompatActivity  implements View.OnClickListener {
    private ImageButton bHelp;
    private ProgressBar progressBarTapping;
    private ImageView iEmojin;
    private TextView tmessage_tapping, tmessage_tapping_perc;
    FeatureDA perc_sliding;
    FeatureDataService feat_data_service;

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
        SetListeners();

        progressBarTapping=findViewById(R.id.bar_tapping);
        iEmojin=findViewById(R.id.iEmojin_tapping);
        tmessage_tapping=findViewById(R.id.tmessage_tapping);
        tmessage_tapping_perc=findViewById(R.id.tmessage_tapping_perc);

        RadarFigureManager RadarManager = new RadarFigureManager(this);
        // Radar chart
        RadarChart radarchart= findViewById(R.id.chart_tapping);

        feat_data_service=new FeatureDataService(this);

        List<FeatureDA> perc_tap= new ArrayList<>();
        perc_tap.add(feat_data_service.get_last_feat_value(feat_data_service.perc_tapping1_name));
        perc_tap.add(feat_data_service.get_last_feat_value(feat_data_service.perc_tapping2_name));
        float per_tap_val=feat_data_service.get_avg_feat(perc_tap);


        List<FeatureDA> vel_tap= new ArrayList<>();
        vel_tap.add(feat_data_service.get_last_feat_value(feat_data_service.veloc_tapping1_name));
        vel_tap.add(feat_data_service.get_last_feat_value(feat_data_service.veloc_tapping2_name));
        float vel_tap_val=feat_data_service.get_avg_feat(vel_tap);

        List<FeatureDA> prec_tap= new ArrayList<>();
        prec_tap.add(feat_data_service.get_last_feat_value(feat_data_service.precision_tapping1_name));
        prec_tap.add(feat_data_service.get_last_feat_value(feat_data_service.precision_tapping2_name));
        float prec_tap_val=feat_data_service.get_avg_feat(prec_tap);

        perc_sliding =feat_data_service.get_last_feat_value(feat_data_service.perc_sliding_name);
        float perc_slidingval=perc_sliding.getFeature_value();

        float[] data1 ={per_tap_val, vel_tap_val, prec_tap_val, perc_slidingval};
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
            Date current = Calendar.getInstance().getTime();
            FeatureDA area_tapping=new FeatureDA(feat_data_service.area_tapping_name, current, (float)area_progress );
            feat_data_service.save_feature(area_tapping);

        }



        RadarManager.put_emojin_and_message(iEmojin, tmessage_tapping, tmessage_tapping_perc, area_progress, progressBarTapping, this);





    }

    private void SetListeners(){
        bHelp.setOnClickListener(this);
    }




    @Override
    public void onClick(View view) {

        switch (view.getId()){
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
