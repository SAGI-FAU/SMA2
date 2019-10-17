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


public class ResultsMovement extends AppCompatActivity  implements View.OnClickListener {
    Button bHistory;
    private ImageButton bHelp;
    private ProgressBar progressBarMovement;
    private ImageView iEmojin;
    private TextView tmessage_movement, tmessage_movement_perc;
    int screenWidth, screenHeight;
    FeatureDA freeze_index;
    FeatureDA posture;
    FeatureDA postural_tremor_right;
    FeatureDA postural_tremor_left;
    FeatureDA kinetic_reg_right;
    FeatureDA kinetic_reg_left;
    FeatureDA rotation_reg_right;
    FeatureDA rotation_reg_left;
    FeatureDA circles_reg_right;
    FeatureDA circles_reg_left;
    FeatureDA n_steps;
    FeatureDA duarion_steps;
    FeatureDataService feat_data_service;

    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";

    //"/storage/emulated/0/AppSpeechData/ACC/Tapping_example.csv";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // general objects
        setContentView(R.layout.activity_walking_feature);
        bHelp=findViewById(R.id.button_help);
        bHelp.bringToFront();
        bHistory=findViewById(R.id.button_history);
        SetListeners();

        progressBarMovement=findViewById(R.id.bar_movement);
        iEmojin=findViewById(R.id.iEmojin_movement);
        tmessage_movement=findViewById(R.id.tmessage_movement);
        tmessage_movement_perc=findViewById(R.id.tmessage_movement_perc);
        getDisplayDimensions();

        RadarFigureManager RadarManager = new RadarFigureManager(this);
        // Radar chart
        RadarChart radarchart= findViewById(R.id.chart_movement);

        feat_data_service=new FeatureDataService(this);


        freeze_index=feat_data_service.get_last_feat_value(feat_data_service.freeze_index_name);
        float freeze_index_value=freeze_index.getFeature_value();
        posture=feat_data_service.get_last_feat_value(feat_data_service.posture_name);
        float posture_value=posture.getFeature_value();
        n_steps=feat_data_service.get_last_feat_value(feat_data_service.N_strides_name);
        float n_steps_value=n_steps.getFeature_value();
        duarion_steps=feat_data_service.get_last_feat_value(feat_data_service.duration_strides_name);
        float duration_steps_value=duarion_steps.getFeature_value();

        List<FeatureDA> tremor= new ArrayList<>();
        tremor.add(feat_data_service.get_last_feat_value(feat_data_service.tremor_left_name));
        tremor.add(feat_data_service.get_last_feat_value(feat_data_service.tremor_right_name));
        float tremor_val=feat_data_service.get_avg_feat(tremor);

        List<FeatureDA> regularity= new ArrayList<>();
        regularity.add(feat_data_service.get_last_feat_value(feat_data_service.regularity_circles_left_name));
        regularity.add(feat_data_service.get_last_feat_value(feat_data_service.regularity_circles_right_name));
        regularity.add(feat_data_service.get_last_feat_value(feat_data_service.regularity_kinetic_left_name));
        regularity.add(feat_data_service.get_last_feat_value(feat_data_service.regularity_kinetic_right_name));
        regularity.add(feat_data_service.get_last_feat_value(feat_data_service.regularity_pronation_left_name));
        regularity.add(feat_data_service.get_last_feat_value(feat_data_service.regularity_pronation_right_name));
        float regularity_val=feat_data_service.get_avg_feat(regularity);


        float[] data1 ={tremor_val, regularity_val, freeze_index_value, posture_value, n_steps_value, duration_steps_value};
        float[] data2={100f,100f,100f,100f, 100f, 100f};

        String Label_1 = getResources().getString(R.string.tremor);
        String Label_2 = getResources().getString(R.string.regularity);
        String Label_3 = getResources().getString(R.string.freezeIndex);
        String Label_4 = getResources().getString(R.string.posture);
        String Label_5 = getResources().getString(R.string.nStrides);
        String Label_6 = getResources().getString(R.string.tStrides);
        String[] labels={Label_1, Label_2, Label_3, Label_4, Label_5, Label_6};

        RadarManager.PlotRadar(radarchart, data1, data2, labels);

        double area=RadarManager.get_area_chart(data1);
        double maxArea=RadarManager.get_area_chart(data2);
        int area_progress=(int)(area*100/maxArea);

        SharedPreferences sharedPref =PreferenceManager.getDefaultSharedPreferences(this);
        boolean new_area_movement=sharedPref.getBoolean("New Area Movement", false);


        if (new_area_movement){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("New Area Movement", false);
            editor.apply();
            Date current = Calendar.getInstance().getTime();
            FeatureDA area_movement=new FeatureDA(feat_data_service.area_movement_name, current, (float)area_progress );
            feat_data_service.save_feature(area_movement);

        }


        LinearLayout.LayoutParams params_line= (LinearLayout.LayoutParams)  iEmojin.getLayoutParams();
        int xRandomBar= (int)(0.01*area_progress*screenWidth-45);

        params_line.setMarginStart(xRandomBar); // The indicator bar position
        params_line.leftMargin=xRandomBar;
        params_line.setMarginStart(xRandomBar);
        iEmojin.setLayoutParams(params_line);

        progressBarMovement.setProgress(area_progress);
        String msgp=String.valueOf(area_progress)+"%";
        tmessage_movement_perc.setText(msgp);
        if (area_progress >=66) {
            iEmojin.setImageResource(R.drawable.happy_emojin);
            tmessage_movement.setText(R.string.Positive_message);
        }
        else if (area_progress>=33){
            iEmojin.setImageResource(R.drawable.medium_emojin);
            tmessage_movement.setText(R.string.Medium_message);
        }
        else{
            iEmojin.setImageResource(R.drawable.sad_emoji);
            tmessage_movement.setText(R.string.Negative_message);
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

        String Text = getResources().getString(R.string.MovementHelp);
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