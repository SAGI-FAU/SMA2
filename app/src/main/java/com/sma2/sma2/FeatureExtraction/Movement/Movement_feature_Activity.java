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
import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Movement_feature_Activity extends AppCompatActivity implements View.OnClickListener {

    Button bBack;
    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";
    String path_movementRight = null, path_movementLeft = null;
    List<String> path_movement_all_right= new ArrayList<String>(), path_movement_all_left= new ArrayList<String>();
    TextView tTremor_left, tTremor_right;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_feature_);
        bBack=findViewById(R.id.button_back5);
        bBack.setOnClickListener(this);
        tTremor_left=findViewById(R.id.tTremor_left);
        tTremor_right=findViewById(R.id.tTremor_right);
        SignalDataService signalDataService =new SignalDataService(this);
        CSVFileReader FileReader=new CSVFileReader(this);
        MovementProcessing MovementProcessor=new MovementProcessing();
        DecimalFormat df = new DecimalFormat("#.0");



        double TremorRight=0;
        List<SignalDA> SignalsRight=signalDataService.getSignalsbyname("Postural tremor Right");
        if (SignalsRight.size()>0){
            path_movementRight=PATH+SignalsRight.get(SignalsRight.size()-1).getSignalPath();

            if (SignalsRight.size()>4){
                for (int i=3;i>=0;i--){
                    path_movement_all_right.add(PATH+SignalsRight.get(i).getSignalPath());
                }
            }
            else{
                for (int i=SignalsRight.size()-1;i>=0;i--){
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




        double TremorLeft=0;
        List<SignalDA> SignalsLeft=signalDataService.getSignalsbyname("Postural tremor Left");
        if (SignalsLeft.size()>0){
            path_movementLeft=PATH+SignalsLeft.get(SignalsLeft.size()-1).getSignalPath();

            if (SignalsLeft.size()>4){
                for (int i=3;i>=0;i--){
                    path_movement_all_left.add(PATH+SignalsLeft.get(i).getSignalPath());
                }
            }
            else{
                for (int i=SignalsLeft.size()-1;i>=0;i--){
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








        int j;
        BarGraphSeries<DataPoint> series= new BarGraphSeries<>();
        if (path_movement_all_left.size()>0) {
            j = path_movement_all_left.size() - 1;
            for (int i = 0; i < path_movement_all_left.size(); i++) {

                CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movement_all_left.get(i), "aX [m/s^2]");
                CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movement_all_left.get(i), "aY [m/s^2]");
                CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movement_all_left.get(i), "aZ [m/s^2]");
                TremorLeft = MovementProcessor.ComputeTremor(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal);

                series.appendData(new DataPoint(i + 1, TremorLeft), true, 5);
                j = j - 1;
            }
        }
        else{
            series.appendData(new DataPoint(1, 0), true, 5);
        }
        GraphView graph =findViewById(R.id.bar_percTremorLeft);
        graph.addSeries(series);

        series.setColor(Color.rgb(255, 140, 0));
        series.setSpacing(5);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        series.setTitle(getResources().getString(R.string.TremorAmplitudeLeft));
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(getResources().getString(R.string.session));
        gridLabel.setVerticalAxisTitle(getResources().getString(R.string.TremorAmplitudeLeft));





        BarGraphSeries<DataPoint> series2= new BarGraphSeries<>();
        if (path_movement_all_right.size()>0) {
            j = path_movement_all_right.size() - 1;
            for (int i = 0; i < path_movement_all_right.size(); i++) {

                CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movement_all_right.get(i), "aX [m/s^2]");
                CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movement_all_right.get(i), "aY [m/s^2]");
                CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movement_all_right.get(i), "aZ [m/s^2]");
                TremorRight = MovementProcessor.ComputeTremor(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal);

                series2.appendData(new DataPoint(i + 1, TremorRight), true, 5);
                j = j - 1;
            }
        }
        else{
            series2.appendData(new DataPoint(1, 0), true, 5);
        }
        GraphView graph2 =findViewById(R.id.bar_percTremorRight);
        graph2.addSeries(series2);

        series2.setColor(Color.rgb(255, 140, 0));
        series2.setSpacing(5);
        graph2.getViewport().setMinY(0.0);
        graph2.getViewport().setMinX(0);
        graph2.getViewport().setMaxX(5);
        graph2.getViewport().setYAxisBoundsManual(true);
        graph2.getViewport().setXAxisBoundsManual(true);
        series2.setTitle(getResources().getString(R.string.TremorAmplitudeRight));
        GridLabelRenderer gridLabel2 = graph.getGridLabelRenderer();
        gridLabel2.setHorizontalAxisTitle(getResources().getString(R.string.session));
        gridLabel2.setVerticalAxisTitle(getResources().getString(R.string.TremorAmplitudeRight));



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
        Intent i =new Intent(Movement_feature_Activity.this, MainActivityMenu.class);
        startActivity(i);

    }

}
