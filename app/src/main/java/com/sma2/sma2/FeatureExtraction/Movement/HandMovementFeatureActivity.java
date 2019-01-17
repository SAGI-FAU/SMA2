package com.sma2.sma2.FeatureExtraction.Movement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;
import com.sma2.sma2.FeatureExtraction.GetExercises;
import com.sma2.sma2.FeatureExtraction.GraphManager;
import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HandMovementFeatureActivity extends AppCompatActivity implements View.OnClickListener {

    Button bBack;
    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";
    String path_movementRight = null, path_movementLeft = null;
    List<String> path_movement_all_right= new ArrayList<String>(), path_movement_all_left= new ArrayList<String>();
    TextView tTremor_left, tTremor_right, title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features_hand_movement);
        bBack=findViewById(R.id.button_back5);
        bBack.setOnClickListener(this);
        tTremor_left=findViewById(R.id.tTremor_left);
        tTremor_right=findViewById(R.id.tTremor_right);
        title=findViewById(R.id.textView_movement2);
        SignalDataService signalDataService =new SignalDataService(this);
        CSVFileReader FileReader=new CSVFileReader(this);
        MovementProcessing MovementProcessor=new MovementProcessing();
        DecimalFormat df = new DecimalFormat("#.0");


        Intent intent = getIntent();
        int IDEx=intent.getIntExtra("IDright",0);
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);

        String titleEx;
        if (name.contains("Left"))
            titleEx=name.replace("-Left", "");
        else
            titleEx=name.replace("-Right", "");
        title.setText(titleEx);

        double StabRight, StabRight_t, StabLeft, StabLeft_t;

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

        float[] f0right={0};
        float[] tright={0};
        if(path_movementRight==null){
            tTremor_right.setText(R.string.Empty);
        }
        else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementRight, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementRight, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementRight, "aZ [m/s^2]");

            f0right = MovementProcessor.ComputeF0(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal, 100);
            StabRight=100-MovementProcessor.perc_stab_freq(f0right);
            tTremor_right.setText(String.valueOf(df.format(StabRight))+"%");

            tright= new float[f0right.length];
            float t=1f;
            for (int j=0;j<f0right.length;j++){
                tright[j]=t;
                t+=1;
            }

        }


        IDEx=intent.getIntExtra("IDleft",0);
        GetEx=new GetExercises(this);
        name=GetEx.getNameExercise(IDEx);

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

        float[] f0left={0}, tleft={0};
        if(path_movementLeft==null){
            tTremor_left.setText(R.string.Empty);

        }
        else {
            CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movementLeft, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movementLeft, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movementLeft, "aZ [m/s^2]");
            f0left = MovementProcessor.ComputeF0(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal, 100);
            StabLeft=100-MovementProcessor.perc_stab_freq(f0left);
            tTremor_left.setText(String.valueOf(df.format(StabLeft))+"%");

            tleft= new float[f0left.length];
            float t=1f;
            for (int j=0;j<f0left.length;j++){
                tleft[j]=t;
                t+=1;
            }
        }



        GraphManager graphManager=new GraphManager(this);
        ArrayList<Integer> xl=new ArrayList<>();
        ArrayList<Float> yl=new ArrayList<>();
        float[] f0left_t, f0right_t;
        for (int i=0;i<5;i++){

            if (i<path_movement_all_left.size()){
                CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movement_all_left.get(i), "aX [m/s^2]");
                CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movement_all_left.get(i), "aY [m/s^2]");
                CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movement_all_left.get(i), "aZ [m/s^2]");

                f0left_t = MovementProcessor.ComputeF0(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal, 100);
                StabLeft_t=100-MovementProcessor.perc_stab_freq(f0left_t);
                xl.add(i+1);
                yl.add((float)StabLeft_t);
            }
            else{
                xl.add(i+1);
                yl.add((float) 0);
            }

        }

        String Title=getResources().getString(R.string.left_stability);
        String Ylabel=getResources().getString(R.string.percentage);
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
                f0right_t = MovementProcessor.ComputeF0(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal, 100);
                StabRight_t=100-MovementProcessor.perc_stab_freq(f0right_t);
                xr.add(i+1);
                yr.add((float)StabRight_t);
            }
            else{
                xr.add(i+1);
                yr.add((float) 0);
            }

        }
        Title=getResources().getString(R.string.right_stability);
        Ylabel=getResources().getString(R.string.percentage);
        GraphView graph2 =findViewById(R.id.bar_percTremorRight);
        graphManager.BarGraph(graph2, xr, yr, 0, 5, Title, Xlabel, Ylabel);





        GraphView graph3 =findViewById(R.id.plotlineF);
        Title=getResources().getString(R.string.F0);

        Xlabel=getResources().getString(R.string.time);
        Ylabel=getResources().getString(R.string.frequency);
        String leg1=getResources().getString(R.string.left);
        String leg2=getResources().getString(R.string.right);
        graphManager.LineGraph2lines(graph3, tleft, f0left, tright, f0right, 3, tleft.length, Title, Xlabel, Ylabel, leg1, leg2);

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
        Intent i =new Intent(HandMovementFeatureActivity.this, MainActivityMenu.class);
        startActivity(i);

    }


}
