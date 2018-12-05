package com.sma2.sma2.FeatureExtraction.Movement;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;
import com.sma2.sma2.FeatureExtraction.GetExercises;
import com.sma2.sma2.FeatureExtraction.GraphManager;
import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PosturalTremor_feature_Activity extends AppCompatActivity implements View.OnClickListener {

    Button bBack;
    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";
    String path_movementRight = null, path_movementLeft = null;
    List<String> path_movement_all_right= new ArrayList<String>(), path_movement_all_left= new ArrayList<String>();
    TextView tTremor_left, tTremor_right;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postural_tremor_feature_);
        bBack=findViewById(R.id.button_back5);
        bBack.setOnClickListener(this);
        tTremor_left=findViewById(R.id.tTremor_left);
        tTremor_right=findViewById(R.id.tTremor_right);
        SignalDataService signalDataService =new SignalDataService(this);
        CSVFileReader FileReader=new CSVFileReader(this);
        MovementProcessing MovementProcessor=new MovementProcessing();
        DecimalFormat df = new DecimalFormat("#.0");

        int IDEx=29;
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);

        double TremorRight=0;
        List<SignalDA> SignalsRight=signalDataService.getSignalsbyname(name);
        if (SignalsRight.size()>0){
            path_movementRight=PATH+SignalsRight.get(SignalsRight.size()-1).getSignalPath();

            if (SignalsRight.size()>4){
                for (int i=SignalsRight.size()-4;i<SignalsRight.size();i++){
                    path_movement_all_right.add(PATH+SignalsRight.get(i).getSignalPath());
                }
            }
            else{
                for (int i=0;i<SignalsRight.size();i++){
                    path_movement_all_right.add(PATH+SignalsRight.get(i).getSignalPath());
                }
            }

        }


        if(path_movementRight==null){
            tTremor_right.setText(R.string.Empty);
        }
        else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementRight, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementRight, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementRight, "aZ [m/s^2]");
            TremorRight = MovementProcessor.ComputeTremor(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
            tTremor_right.setText(String.valueOf(df.format(TremorRight)));
        }


        IDEx=30;
        GetEx=new GetExercises(this);
        name=GetEx.getNameExercise(IDEx);

        double TremorLeft=0;
        List<SignalDA> SignalsLeft=signalDataService.getSignalsbyname(name);
        if (SignalsLeft.size()>0){
            path_movementLeft=PATH+SignalsLeft.get(SignalsLeft.size()-1).getSignalPath();

            if (SignalsLeft.size()>4){
                for (int i=SignalsLeft.size()-4;i<SignalsLeft.size();i++){
                    path_movement_all_left.add(PATH+SignalsLeft.get(i).getSignalPath());
                }
            }
            else{
                for (int i=0;i<SignalsLeft.size();i++){
                    path_movement_all_left.add(PATH+SignalsLeft.get(i).getSignalPath());
                }
            }
        }


        if(path_movementLeft==null){
            tTremor_left.setText(R.string.Empty);
        }
        else {
            CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movementLeft, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movementLeft, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movementLeft, "aZ [m/s^2]");
            TremorLeft = MovementProcessor.ComputeTremor(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal);
            tTremor_left.setText(String.valueOf(df.format(TremorLeft)));
        }



        GraphManager graphManager=new GraphManager(this);
        ArrayList<Integer> xl=new ArrayList<>();
        ArrayList<Float> yl=new ArrayList<>();
        for (int i=0;i<5;i++){

            if (i<path_movement_all_left.size()){
                CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movement_all_left.get(i), "aX [m/s^2]");
                CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movement_all_left.get(i), "aY [m/s^2]");
                CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movement_all_left.get(i), "aZ [m/s^2]");
                TremorLeft = MovementProcessor.ComputeTremor(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal);
                xl.add(i+1);
                yl.add((float)TremorLeft);
            }
            else{
                xl.add(i+1);
                yl.add((float) 0);
            }

        }
        String Title=getResources().getString(R.string.TremorAmplitudeLeft);
        String Ylabel=getResources().getString(R.string.TremorAmplitudeLeft);
        String Xlabel=getResources().getString(R.string.session);
        GraphView graph =findViewById(R.id.bar_percTremorLeft);
        graphManager.BarGraph(graph, xl, yl, 0, 5, Title, Xlabel, Ylabel);






        ArrayList<Integer> xr=new ArrayList<>();
        ArrayList<Float> yr=new ArrayList<>();
        for (int i=0;i<5;i++){

            if (i<path_movement_all_right.size()){
                CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movement_all_right.get(i), "aX [m/s^2]");
                CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movement_all_right.get(i), "aY [m/s^2]");
                CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movement_all_right.get(i), "aZ [m/s^2]");
                TremorRight = MovementProcessor.ComputeTremor(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal);
                xr.add(i+1);
                yr.add((float)TremorRight);
            }
            else{
                xr.add(i+1);
                yr.add((float) 0);
            }

        }
        Title=getResources().getString(R.string.TremorAmplitudeRight);
        Ylabel=getResources().getString(R.string.TremorAmplitudeRight);
        GraphView graph2 =findViewById(R.id.bar_percTremorRight);
        graphManager.BarGraph(graph2, xr, yr, 0, 5, Title, Xlabel, Ylabel);



    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_back5:
                onButtonBack();
                break;
        }
    }

    private void onButtonBack(){
        Intent i =new Intent(PosturalTremor_feature_Activity.this, MainActivityMenu.class);
        startActivity(i);

    }

}
