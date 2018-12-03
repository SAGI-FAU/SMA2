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
import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BalanceTremorFeatureActivity extends AppCompatActivity implements View.OnClickListener {

    Button bBack;
    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";
    String path_movement = null;
    List<String> path_movement_all= new ArrayList<>();
    TextView tTremor;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_feature_);
        bBack=findViewById(R.id.button_back6);
        bBack.setOnClickListener(this);
        tTremor=findViewById(R.id.tTremorBalance);
        SignalDataService signalDataService =new SignalDataService(this);
        CSVFileReader FileReader=new CSVFileReader(this);
        MovementProcessing MovementProcessor=new MovementProcessing();
        DecimalFormat df = new DecimalFormat("#.0");



        int IDEx=22;
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);
        double Tremor=0;
        List<SignalDA> Signals=signalDataService.getSignalsbyname(name);
        if (Signals.size()>0){
            path_movement=PATH+Signals.get(Signals.size()-1).getSignalPath();

            if (Signals.size()>4){
                for (int i=Signals.size()-4;i<Signals.size();i++){
                    path_movement_all.add(PATH+Signals.get(i).getSignalPath());
                }
            }
            else{
                for (int i=0;i<Signals.size();i++){
                    path_movement_all.add(PATH+Signals.get(i).getSignalPath());
                }
            }

        }


        if(path_movement==null){
            tTremor.setText(R.string.Empty);
        }
        else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movement, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movement, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movement, "aZ [m/s^2]");
            Tremor = MovementProcessor.ComputeTremor(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
            tTremor.setText(String.valueOf(df.format(Tremor)));
        }


        BarGraphSeries<DataPoint> series= new BarGraphSeries<>();
        if (path_movement_all.size()>0) {
            for (int i = 0; i < path_movement_all.size(); i++) {

                CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movement_all.get(i), "aX [m/s^2]");
                CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movement_all.get(i), "aY [m/s^2]");
                CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movement_all.get(i), "aZ [m/s^2]");
                Tremor = MovementProcessor.ComputeTremor(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal);

                series.appendData(new DataPoint(i + 1, Tremor), true, 5);
            }
        }
        else{
            series.appendData(new DataPoint(1, 0), true, 5);
        }
        GraphView graph =findViewById(R.id.bar_TremorBalance);
        graph.addSeries(series);

        series.setColor(Color.rgb(255, 140, 0));
        series.setSpacing(5);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        series.setTitle(getResources().getString(R.string.TremorAmplitude));
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(getResources().getString(R.string.session));
        gridLabel.setVerticalAxisTitle(getResources().getString(R.string.TremorAmplitude));
        gridLabel.setNumHorizontalLabels(5);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_back6:
                onButtonBack();
                break;
        }
    }

    private void onButtonBack(){
        Intent i =new Intent(BalanceTremorFeatureActivity.this, MainActivityMenu.class);
        startActivity(i);

    }





}
