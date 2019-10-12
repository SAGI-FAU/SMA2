package com.sma2.sma2.FeatureExtraction.Movement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.github.mikephil.charting.charts.RadarChart;
import com.sma2.sma2.FeatureExtraction.GraphManager;
import com.sma2.sma2.FeatureExtraction.Speech.features.RadarFeatures;
import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;
import com.sma2.sma2.RadarFigureManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WalkingFeatureActivity extends AppCompatActivity implements View.OnClickListener {

    Button bBack;
    private ImageButton bHelp;

    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";
    String path_movement = null;
    List<String> path_movement_all = new ArrayList<>();
    TextView tWalking;
    //private double maxArea = 23776; //Five features
    private double maxArea = 25980.76; //Six features

    int screenWidth, screenHeight;

    private float tremorValue = 0;
    private float regularityValue = 0;
    private float freezeValue = 0;
    private float postureValue = 0;
    private float nStridesValue = 0;
    private float tStrideValues = 0;

    TextView tVelocity;
    private ProgressBar progressBarMov;
    private ImageView iEmojin;
    private TextView tmessage_Mov, tmessage_Mov_perc;
    private RadarFigureManager RadarManager;
    String TAG = this.getClass().getName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking_feature);
        bBack = findViewById(R.id.button_back_result_mov);
        bHelp=findViewById(R.id.button_help_Mov);

        progressBarMov = findViewById(R.id.bar_mov);
        iEmojin = findViewById(R.id.iEmojin_Mov);
        tmessage_Mov = findViewById(R.id.tmessage_mov);
        tmessage_Mov_perc = findViewById(R.id.tmessage_mov_perc);
        getDisplayDimensions();
        SetListeners();
        RadarManager = new RadarFigureManager(this);
        // Radar chart
        RadarChart radarchart = findViewById(R.id.chart2);

        ArrayList<Float> area_perf = new ArrayList<>();

        try {
            area_perf = RadarFeatures.get_feat_perf("area movement");
        } catch (IOException ie) {
            ie.printStackTrace();
            area_perf.add(0f);
        }



        //Feature Values

        freezeValue=0f;

        //Upper tremor and regularity values

        // Compute Tremor

        ArrayList<Float> PostTremorRight=new ArrayList<>();
        try {
            PostTremorRight= RadarFeatures.get_feat_perf("Postural_Tremor_Right");
        }
        catch(IOException ie) {
            PostTremorRight.add(0f);
            ie.printStackTrace();
        }

        ArrayList<Float> PosturalTremorLeft=new ArrayList<>();
        try {
            PosturalTremorLeft= RadarFeatures.get_feat_perf("Postural_Tremor_Left");
        }
        catch(IOException ie) {
            PosturalTremorLeft.add(0f);
            ie.printStackTrace();
        }

        ArrayList<Float> KineticTremorRight=new ArrayList<>();
        try {
            KineticTremorRight= RadarFeatures.get_feat_perf("Kinetic_Tremor_Right");
        }
        catch(IOException ie) {
            KineticTremorRight.add(0f);
            ie.printStackTrace();
        }

        ArrayList<Float> KineticTremorLeft=new ArrayList<>();
        try {
            KineticTremorLeft= RadarFeatures.get_feat_perf("Kinetic_Tremor_Left");
        }
        catch(IOException ie) {
            KineticTremorLeft.add(0f);
            ie.printStackTrace();
        }

        float posturaltremorleft = PosturalTremorLeft.get(PosturalTremorLeft.size()-1);
        float posturaltremorright = PostTremorRight.get(PostTremorRight.size()-1);
        float movementtremorleft = KineticTremorLeft.get(KineticTremorLeft.size()-1);
        float movementtremorright = KineticTremorRight.get(KineticTremorRight.size()-1);
        tremorValue=(posturaltremorleft+posturaltremorright+movementtremorleft+movementtremorright)/4;

        // Compute Regularity
        ArrayList<Float> KineticRegularityRight=new ArrayList<>();
        try {
            KineticRegularityRight= RadarFeatures.get_feat_perf("Kinetic_Regularity_Right");
        }
        catch(IOException ie) {
            KineticRegularityRight.add(0f);
            ie.printStackTrace();
        }

        ArrayList<Float> KineticRegularityLeft=new ArrayList<>();
        try {
            KineticRegularityLeft= RadarFeatures.get_feat_perf("Kinetic_Regularity_Left");
        }
        catch(IOException ie) {
            KineticRegularityLeft.add(0f);
            ie.printStackTrace();
        }

        ArrayList<Float> RotationRegularityRight=new ArrayList<>();
        try {
            RotationRegularityRight= RadarFeatures.get_feat_perf("Regularity_Rotation_Right");
        }
        catch(IOException ie) {
            RotationRegularityRight.add(0f);
            ie.printStackTrace();
        }

        ArrayList<Float> RotationRegularityLeft=new ArrayList<>();
        try {
            RotationRegularityLeft= RadarFeatures.get_feat_perf("Regularity_Rotation_Left");
        }
        catch(IOException ie) {
            RotationRegularityLeft.add(0f);
            ie.printStackTrace();
        }

        ArrayList<Float> CirclesRegularityRight=new ArrayList<>();
        try {
            CirclesRegularityRight= RadarFeatures.get_feat_perf("Regularity_Circles_Right");
        }
        catch(IOException ie) {
            CirclesRegularityRight.add(0f);
            ie.printStackTrace();
        }

        ArrayList<Float> CirclesRegularityLeft=new ArrayList<>();
        try {
            CirclesRegularityLeft= RadarFeatures.get_feat_perf("Regularity_Circles_Left");
        }
        catch(IOException ie) {
            CirclesRegularityLeft.add(0f);
            ie.printStackTrace();
        }

        float StabKinLeft = KineticRegularityLeft.get(KineticRegularityLeft.size()-1);
        float StabKinRight = KineticRegularityRight.get(KineticRegularityRight.size()-1);
        float StabRotLeft = RotationRegularityLeft.get(RotationRegularityLeft.size()-1);
        float StabRotRight = RotationRegularityRight.get(RotationRegularityRight.size()-1);
        float StabCirLeft = CirclesRegularityLeft.get(CirclesRegularityLeft.size()-1);
        float StabCirRigth = CirclesRegularityRight.get(CirclesRegularityRight.size()-1);
        regularityValue = (StabKinLeft+StabKinRight+StabRotLeft+StabRotRight+StabCirLeft+StabCirRigth)/6;


        float[] data1 = {tremorValue, regularityValue, freezeValue, postureValue, nStridesValue, tStrideValues}; // Patient


        float[] data2 = {(float) 50, (float) 50, (float) 50, (float) 50, (float) 50, (float) 50}; // Healthy

        String Label_1 = getResources().getString(R.string.tremor);
        String Label_2 = getResources().getString(R.string.regularity);
        String Label_3 = getResources().getString(R.string.freezeIndex);
        String Label_4 = getResources().getString(R.string.posture);
        String Label_5 = getResources().getString(R.string.nStrides);
        String Label_6 = getResources().getString(R.string.tStrides);

        String[] labels = {Label_1, Label_2, Label_3, Label_4, Label_5, Label_6};


        RadarManager.PlotRadar(radarchart, data1, data2, labels);


        double area = RadarManager.get_area_chart(data1);

        int area_progress = (int) (area * 100 / maxArea);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean new_area_mov = sharedPref.getBoolean("New Area Mov", false);

        if (new_area_mov) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("New Area Movement", false);
            editor.apply();
            try {
                RadarFeatures.export_speech_feature("AreaMov", area_progress, "area mov");
            } catch (Exception e) {
                Toast.makeText(this, R.string.jitter_failed, Toast.LENGTH_SHORT).show();

            }

        }


        ConstraintLayout.LayoutParams params_line = (ConstraintLayout.LayoutParams) iEmojin.getLayoutParams();


        int xRandomBar = (int) (0.01 * area_progress * screenWidth - 45);



        params_line.setMarginStart(xRandomBar); // The indicator bar position
        params_line.leftMargin = xRandomBar;
        params_line.setMarginStart(xRandomBar);
        iEmojin.setLayoutParams(params_line);


        progressBarMov.setProgress(area_progress);
        String msgp = "Performance: " + String.valueOf(area_progress) + "%";
        tmessage_Mov_perc.setText(msgp);
        if (area_progress >= 66) {
            iEmojin.setImageResource(R.drawable.happy_emojin);
            tmessage_Mov.setText(R.string.Positive_message);
        } else if (area_progress >= 33) {
            iEmojin.setImageResource(R.drawable.medium_emojin);
            tmessage_Mov.setText(R.string.Medium_message);
        } else {
            iEmojin.setImageResource(R.drawable.sad_emoji);
            tmessage_Mov.setText(R.string.Negative_message);
        }
        GraphManager graphManager = new GraphManager(this);
        String Title = getResources().getString(R.string.speech_performance);
        String Ylabel = getResources().getString(R.string.percentage);
        String Xlabel = getResources().getString(R.string.session);
        GraphView graph = findViewById(R.id.plotlineMovement);

        ArrayList<Integer> xl = new ArrayList<>();
        for (int i = 0; i < area_perf.size(); i++) {
            xl.add(i + 1);
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

    private void SetListeners() {
        bBack.setOnClickListener(this);
        bHelp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_back_result:
                onButtonBack();
                break;
            case R.id.button_help_Mov:
                onButtonHelp();
                break;
        }
    }

    private void onButtonBack() {
        Intent i = new Intent(WalkingFeatureActivity.this, MainActivityMenu.class);
        startActivity(i);
    }

    private void onButtonHelp() {
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






