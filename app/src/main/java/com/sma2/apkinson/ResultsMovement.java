package com.sma2.apkinson;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sma2.apkinson.DataAccess.FeatureDA;
import com.sma2.apkinson.DataAccess.FeatureDataService;
import com.sma2.apkinson.FeatureExtraction.Movement.MovementProcessing;

import com.github.mikephil.charting.charts.RadarChart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ResultsMovement extends AppCompatActivity implements View.OnClickListener {
    private ImageButton bHelp;
    private ProgressBar progressBarMovement;
    private ImageView iEmojin;
    private TextView tmessage_movement, tmessage_movement_perc;
    FeatureDA freeze_index;
    FeatureDA posture;
    FeatureDA n_steps;
    FeatureDA duarion_steps;
    FeatureDataService feat_data_service;
    MovementProcessing movementProcessing;

    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // general objects
        setContentView(R.layout.activity_walking_feature);
        bHelp=findViewById(R.id.button_help);
        bHelp.bringToFront();
        SetListeners();
        movementProcessing= new MovementProcessing();

        progressBarMovement=findViewById(R.id.bar_movement);
        iEmojin=findViewById(R.id.iEmojin_movement);
        tmessage_movement=findViewById(R.id.tmessage_movement);
        tmessage_movement_perc=findViewById(R.id.tmessage_movement_perc);

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

        float perc_steps_value= movementProcessing.n_steps2perc(n_steps_value);

        duarion_steps=feat_data_service.get_last_feat_value(feat_data_service.duration_strides_name);
        float duration_steps_value=duarion_steps.getFeature_value();
        float perc_duration_value= movementProcessing.duration2perc(duration_steps_value);


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


        float[] data1 ={tremor_val, regularity_val, freeze_index_value, posture_value, perc_steps_value, perc_duration_value};
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

        RadarManager.put_emojin_and_message(iEmojin, tmessage_movement, tmessage_movement_perc, area_progress, progressBarMovement, this);


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
