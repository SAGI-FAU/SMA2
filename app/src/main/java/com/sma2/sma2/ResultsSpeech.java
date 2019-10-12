package com.sma2.sma2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.RadarChart;
import com.sma2.sma2.FeatureExtraction.Speech.features.RadarFeatures;


public class ResultsSpeech extends AppCompatActivity implements View.OnClickListener{

    private Results.OnFragmentInteractionListener mListener;
    private Button bHistory;
    private RadarFigureManager RadarManager;
    private ProgressBar progressBar;
    private ImageView iEmojin;
    private TextView tmessage, tmessage_perc;
    int screenWidth, screenHeight;
    private ImageButton bHelp;
    private double maxArea=23776;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_results_speech);

        bHistory=findViewById(R.id.button_history);
        progressBar=findViewById(R.id.bar_speech);
        iEmojin=findViewById(R.id.iEmojin_speech);
        tmessage=findViewById(R.id.tmessage_speech);
        tmessage_perc=findViewById(R.id.tmessage_speech_perc);
        bHelp=findViewById(R.id.button_help);
        bHelp.bringToFront();
        getDisplayDimensions();
        setListeners();
        RadarManager = new RadarFigureManager(this);
        RadarChart radarchart= findViewById(R.id.chart_speech);

        float jitter= RadarFeatures.get_last_feature("Jitter");
        float Vrate= RadarFeatures.get_last_feature("VRate");
        float intonation= RadarFeatures.get_last_feature("Intonation");
        float wer= 0f; //TODO: wer from the server
        float pron= 0f; //TODO: pron from the server

        float[] data1={pron, jitter,Vrate,intonation, wer}; // Patient
        float[] data2={86f,98f,82.2f,55.4f, 100f}; // Healthy

        String Label_1 = getResources().getString(R.string.pronunciation);
        String Label_2 = getResources().getString(R.string.stability);
        String Label_3 = getResources().getString(R.string.rate);
        String Label_4 = getResources().getString(R.string.intonation);
        String Label_5 = getResources().getString(R.string.intelligibility);
        String[] labels={Label_1, Label_2, Label_3, Label_4, Label_5};

        RadarManager.PlotRadar(radarchart, data1, data2, labels);
        double area=RadarManager.get_area_chart(data1);
        int area_progress=(int)(area*100/maxArea);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean new_area_speech=sharedPref.getBoolean("New Area Speech", false);

        if (new_area_speech){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("New Area Speech", false);
            editor.apply();
            try {
                RadarFeatures.export_speech_feature("AreaSpeech", area_progress, "area speech");
            }catch (Exception e) {
                Toast.makeText(this,R.string.area_failed,Toast.LENGTH_SHORT).show();

            }
        }

        LinearLayout.LayoutParams params_line= (LinearLayout.LayoutParams)  iEmojin.getLayoutParams();
        int xRandomBar= (int)(0.01*area_progress*screenWidth-45);

        params_line.setMarginStart(xRandomBar); // The indicator bar position
        params_line.leftMargin=xRandomBar;
        params_line.setMarginStart(xRandomBar);
        iEmojin.setLayoutParams(params_line);

        progressBar.setProgress(area_progress);
        String msgp=String.valueOf(area_progress)+"%";
        tmessage_perc.setText(msgp);
        if (area_progress >=66) {
            iEmojin.setImageResource(R.drawable.happy_emojin);
            tmessage.setText(R.string.Positive_message);
        }
        else if (area_progress>=33){
            iEmojin.setImageResource(R.drawable.medium_emojin);
            tmessage.setText(R.string.Medium_message);
        }
        else{
            iEmojin.setImageResource(R.drawable.sad_emoji);
            tmessage.setText(R.string.Negative_message);
        }


    }





    private void setListeners() {
        bHelp.setOnClickListener(this);
        bHistory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_help:
                open_help();
                break;
            case R.id.button_history:
                break;

        }

    }

    private void open_help(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String Title = getResources().getString(R.string.interpre);
        builder.setTitle(Title);

        String Text = getResources().getString(R.string.SpeechHelp);
        builder.setMessage(Text);

        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() { // define the 'Cancel' button
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void getDisplayDimensions() {
        Display display = this.getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

}
