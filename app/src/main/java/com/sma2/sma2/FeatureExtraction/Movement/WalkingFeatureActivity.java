package com.sma2.sma2.FeatureExtraction.Movement;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;
import com.sma2.sma2.FeatureExtraction.GetExercises;
import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;
import com.sma2.sma2.RadarFigureManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.github.mikephil.charting.charts.RadarChart;


public class WalkingFeatureActivity extends AppCompatActivity implements View.OnClickListener {

    Button bBack;
    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";
    String path_movement = null;
    List<String> path_movement_all = new ArrayList<>();
    TextView tWalking;
    TextView tVelocity;
    private RadarFigureManager RadarManager;

    String path_movementRight = null, path_movementLeft = null;
    List<String> path_movement_all_right= new ArrayList<String>(), path_movement_all_left= new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking_feature);
        bBack = findViewById(R.id.button_back6);
        bBack.setOnClickListener(this);
        tWalking = findViewById(R.id.tTremorWalking);
        tVelocity = findViewById(R.id.tVelocityWalking);
        SignalDataService signalDataService = new SignalDataService(this);
        CSVFileReader FileReader = new CSVFileReader(this);
        MovementProcessing MovementProcessor = new MovementProcessing();
        DecimalFormat df = new DecimalFormat("#.0");

        int IDEx = 31;   // Gait 4x10
        GetExercises GetEx = new GetExercises(this);

        String name = GetEx.getNameExercise(IDEx);
        double tremor = 0.0d;
        double steps = 0.0d;
        double velocity = 0.0d;
        double movTremor = 0;
        float[] pitch;
        double power = 0.0;

        List<SignalDA> Signals = signalDataService.getSignalsbyname(name);
        if (Signals.size() > 0){
            path_movement = PATH + Signals.get(Signals.size()-1).getSignalPath();

            if (Signals.size() > 4){
                for (int i = Signals.size()- 4; i < Signals.size(); i++){
                    path_movement_all.add(PATH+Signals.get(i).getSignalPath());
                }
            }
            else{
                for (int i = 0; i < Signals.size(); i++){
                    path_movement_all.add(PATH+Signals.get(i).getSignalPath());
                }
            }

        }

        ArrayList<Float> accX = new ArrayList<>();
        ArrayList<Float> accY = new ArrayList<>();
        ArrayList<Float> timeX = new ArrayList<>();
        ArrayList<Float> timeY = new ArrayList<>();

        if(path_movement == null){
            tWalking.setText(R.string.Empty);
            tVelocity.setText(R.string.Empty);
        }
        else {
            CSVFileReader.Signal GaitSignalaX = FileReader.ReadMovementSignal(path_movement, "aX [m/s^2]");
            CSVFileReader.Signal GaitSignalaY = FileReader.ReadMovementSignal(path_movement, "aY [m/s^2]");
            CSVFileReader.Signal GaitSignalaZ = FileReader.ReadMovementSignal(path_movement, "aZ [m/s^2]");

            accX = MovementProcessor.getAccX(GaitSignalaX.Signal);

            steps = MovementProcessor.getSteps(accX);
            velocity = MovementProcessor.getSteps(accX);
            tWalking.setText(String.valueOf(df.format(steps)));
            tVelocity.setText(String.valueOf(df.format(velocity)));
            System.out.println(GaitSignalaX.Signal.size());
        }

        // Compute Tremor
        float posturaltremorleft = CalculatePosturalTremorLeft();
        float posturaltremorright = CalculatePosturalTremorRight();
        float movementtremorleft = CalculateMovementTremorLeft();
        float movementtremorright = CalculateMovementTremorRigth();
        float Tremor_Posterior=(posturaltremorleft+posturaltremorright+movementtremorleft+movementtremorright)/4;

        // Compute Regularity
        float StabKinLeft = CalculateStabKineticLeft();
        float StabKinRight = CalculateStabKineticRight();
        float StabRotLeft = CalculateStabRotationLeft();
        float StabRotRight = CalculateStabRotationRight();
        float StabCirLeft = CalculateStabCirclesLeft();
        float StabCirRigth = CalculateStabCirclesRight();
        double Regularity_Posterior = (StabKinLeft+StabKinRight+StabRotLeft+StabRotRight+StabCirLeft+StabCirRigth)/6;

        RadarManager = new RadarFigureManager(this);
        // Radar chart
        RadarChart radarchart= findViewById(R.id.chart2);

        float[] data1={Tremor_Posterior,(float)Regularity_Posterior,(float) 10,(float) 10}; // Patient
        float[] data2={(float) 80,(float) 80,(float) 80,(float) 80}; // Healthy

        String Label_1 = getResources().getString(R.string.tremorPosterior);
        String Label_2 = getResources().getString(R.string.regularityPosterior);
        String Label_3 = getResources().getString(R.string.velocity);
        String Label_4 = getResources().getString(R.string.tremor);
        String[] labels={Label_1, Label_2, Label_3, Label_4};

        RadarManager.PlotRadar(radarchart, data1, data2, labels);

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
        Intent i =new Intent(WalkingFeatureActivity.this, MainActivityMenu.class);
        startActivity(i);
    }

    //--------START FUNCTIONS FOR TREMOR---------------------------------------------------
    // Compute Postural Tremor for both hands
    // Left Hand
    private float CalculatePosturalTremorLeft(){
        double vtremor=0;

        CSVFileReader FileReader=new CSVFileReader(this);
        MovementProcessing MovementProcessor=new MovementProcessing();

        int IDEx=30;
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);

        double TremorLeft=0;

        SignalDataService signalDataService =new SignalDataService(this);
        List<SignalDA> SignalsLeft=signalDataService.getSignalsbyname(name);

        if (SignalsLeft.size()>0){
            path_movementLeft=PATH+SignalsLeft.get(SignalsLeft.size()-1).getSignalPath();

            if (SignalsLeft.size()>1){
                for (int i=SignalsLeft.size()-1;i<SignalsLeft.size();i++){
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
            vtremor=0;
        }
        else {
            CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movementLeft, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movementLeft, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movementLeft, "aZ [m/s^2]");
            TremorLeft = MovementProcessor.ComputeTremor(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal);
            vtremor = 100-TremorLeft;

        }

        return (float) vtremor;
    }

    // Rigth Hand
    private float CalculatePosturalTremorRight(){
        double vtremor=0;

        CSVFileReader FileReader=new CSVFileReader(this);
        MovementProcessing MovementProcessor=new MovementProcessing();

        int IDEx=29;
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);


        double TremorRight=0;
        SignalDataService signalDataService =new SignalDataService(this);
        List<SignalDA> SignalsRight=signalDataService.getSignalsbyname(name);
        if (SignalsRight.size()>0){
            path_movementRight=PATH+SignalsRight.get(SignalsRight.size()-1).getSignalPath();

            if (SignalsRight.size()>1){
                for (int i=SignalsRight.size()-1;i<SignalsRight.size();i++){
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
            vtremor=0;
        }
        else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementRight, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementRight, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementRight, "aZ [m/s^2]");
            TremorRight = MovementProcessor.ComputeTremor(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
            vtremor = 100-TremorRight;
        }

        return (float) vtremor;
    }

    // Compute Movement Tremor for both hands
    // Left Hand
    private float CalculateMovementTremorLeft() {
        double tremormovL = 0;

        CSVFileReader FileReader = new CSVFileReader(this);
        MovementProcessing MovementProcessor = new MovementProcessing();

        int IDEx = 28;
        GetExercises GetEx = new GetExercises(this);
        String name = GetEx.getNameExercise(IDEx);

        SignalDataService signalDataService = new SignalDataService(this);
        List<SignalDA> SignalsLeft = signalDataService.getSignalsbyname(name);

        if (SignalsLeft.size() > 0) {
            path_movementLeft = PATH + SignalsLeft.get(SignalsLeft.size() - 1).getSignalPath();

            if (SignalsLeft.size() > 1) {
                for (int i = SignalsLeft.size() - 1; i < SignalsLeft.size(); i++) {
                    path_movement_all_left.add(PATH + SignalsLeft.get(i).getSignalPath());
                }
            } else {
                for (int i = 0; i < SignalsLeft.size(); i++) {
                    path_movement_all_left.add(PATH + SignalsLeft.get(i).getSignalPath());
                }
            }
        }


        if (path_movementLeft == null) {
            tremormovL = 0;
        } else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementLeft, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementLeft, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementLeft, "aZ [m/s^2]");

            tremormovL = MovementProcessor.TremorMov(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
            tremormovL = 100 - tremormovL;
        }

        return (float) tremormovL;
    }

    // Rigth Hand
    private float CalculateMovementTremorRigth() {
        double tremormovR = 0;

        CSVFileReader FileReader = new CSVFileReader(this);
        MovementProcessing MovementProcessor = new MovementProcessing();

        int IDEx = 27;
        GetExercises GetEx = new GetExercises(this);
        String name = GetEx.getNameExercise(IDEx);


        SignalDataService signalDataService = new SignalDataService(this);
        List<SignalDA> SignalsRight = signalDataService.getSignalsbyname(name);
        if (SignalsRight.size() > 0) {
            path_movementRight = PATH + SignalsRight.get(SignalsRight.size() - 1).getSignalPath();

            if (SignalsRight.size() > 1) {
                for (int i = SignalsRight.size() - 1; i < SignalsRight.size(); i++) {
                    path_movement_all_right.add(PATH + SignalsRight.get(i).getSignalPath());
                }
            } else {
                for (int i = 0; i < SignalsRight.size(); i++) {
                    path_movement_all_right.add(PATH + SignalsRight.get(i).getSignalPath());
                }
            }

        }


        if (path_movementRight == null) {
            tremormovR = 0;
        } else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementRight, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementRight, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementRight, "aZ [m/s^2]");
            tremormovR = MovementProcessor.TremorMov(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
            tremormovR = 100 - tremormovR;

        }

        return (float) tremormovR;

    }
    //--------END FUNCTIONS FOR TREMOR---------------------------------------------------


    //--------START FUNCTIONS FOR STABILITY---------------------------------------------------
    // Compute Stability in Kinetic
    // Left Hand
    private float CalculateStabKineticLeft() {
        double stabVelL=0;

        CSVFileReader FileReader = new CSVFileReader(this);
        MovementProcessing MovementProcessor = new MovementProcessing();

        int IDEx = 28;
        GetExercises GetEx = new GetExercises(this);
        String name = GetEx.getNameExercise(IDEx);

        SignalDataService signalDataService = new SignalDataService(this);
        List<SignalDA> SignalsLeft = signalDataService.getSignalsbyname(name);

        if (SignalsLeft.size() > 0) {
            path_movementLeft = PATH + SignalsLeft.get(SignalsLeft.size() - 1).getSignalPath();

            if (SignalsLeft.size() > 1) {
                for (int i = SignalsLeft.size() - 1; i < SignalsLeft.size(); i++) {
                    path_movement_all_left.add(PATH + SignalsLeft.get(i).getSignalPath());
                }
            } else {
                for (int i = 0; i < SignalsLeft.size(); i++) {
                    path_movement_all_left.add(PATH + SignalsLeft.get(i).getSignalPath());
                }
            }
        }


        if (path_movementLeft == null) {
            stabVelL = 0;
        } else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementLeft, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementLeft, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementLeft, "aZ [m/s^2]");
            stabVelL = MovementProcessor.ComputeRegularity(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
        }
        return (float) stabVelL;

    }
    private float CalculateStabKineticRight() {
        double stabVelR=0;

        CSVFileReader FileReader = new CSVFileReader(this);
        MovementProcessing MovementProcessor = new MovementProcessing();

        int IDEx = 27;
        GetExercises GetEx = new GetExercises(this);
        String name = GetEx.getNameExercise(IDEx);


        SignalDataService signalDataService = new SignalDataService(this);
        List<SignalDA> SignalsRight = signalDataService.getSignalsbyname(name);
        if (SignalsRight.size() > 0) {
            path_movementRight = PATH + SignalsRight.get(SignalsRight.size() - 1).getSignalPath();

            if (SignalsRight.size() > 1) {
                for (int i = SignalsRight.size() - 1; i < SignalsRight.size(); i++) {
                    path_movement_all_right.add(PATH + SignalsRight.get(i).getSignalPath());
                }
            } else {
                for (int i = 0; i < SignalsRight.size(); i++) {
                    path_movement_all_right.add(PATH + SignalsRight.get(i).getSignalPath());
                }
            }

        }

        if (path_movementRight == null) {
            stabVelR = 0;
        } else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementRight, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementRight, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementRight, "aZ [m/s^2]");

            stabVelR = MovementProcessor.ComputeRegularity(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
        }

        return (float) stabVelR;
    }
    // Compute Stability in Rotation
    // Left Hand
    private float CalculateStabRotationLeft(){
        double stabVelL=0;

        CSVFileReader FileReader=new CSVFileReader(this);
        MovementProcessing MovementProcessor=new MovementProcessing();

        int IDEx=26;
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);

        float[] FLeft={0};
        double StabLeft;

        SignalDataService signalDataService =new SignalDataService(this);
        List<SignalDA> SignalsLeft=signalDataService.getSignalsbyname(name);

        if (SignalsLeft.size()>0){
            path_movementLeft=PATH+SignalsLeft.get(SignalsLeft.size()-1).getSignalPath();

            if (SignalsLeft.size()>1){
                for (int i=SignalsLeft.size()-1;i<SignalsLeft.size();i++){
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
            stabVelL=0;
        }
        else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementLeft, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementLeft, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementLeft, "aZ [m/s^2]");

            stabVelL = MovementProcessor.ComputeRegularity(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
        }
        return (float) stabVelL;
    }

    // Rigth Hand
    private float CalculateStabRotationRight(){
        double stabVelR=0;

        CSVFileReader FileReader=new CSVFileReader(this);
        MovementProcessing MovementProcessor=new MovementProcessing();

        int IDEx=25;
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);

        SignalDataService signalDataService =new SignalDataService(this);
        List<SignalDA> SignalsRight=signalDataService.getSignalsbyname(name);
        if (SignalsRight.size()>0){
            path_movementRight=PATH+SignalsRight.get(SignalsRight.size()-1).getSignalPath();

            if (SignalsRight.size()>1){
                for (int i=SignalsRight.size()-1;i<SignalsRight.size();i++){
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
            stabVelR=0;
        }
        else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementRight, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementRight, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementRight, "aZ [m/s^2]");

            stabVelR = MovementProcessor.ComputeRegularity(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
        }

        return (float) stabVelR;

    }

    // Compute Stability in Circles Movement
    // Left Hand
    private float CalculateStabCirclesLeft(){

        double stabVelL=0;

        CSVFileReader FileReader=new CSVFileReader(this);
        MovementProcessing MovementProcessor=new MovementProcessing();

        int IDEx=24;
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);


        SignalDataService signalDataService =new SignalDataService(this);
        List<SignalDA> SignalsLeft=signalDataService.getSignalsbyname(name);

        if (SignalsLeft.size()>0){
            path_movementLeft=PATH+SignalsLeft.get(SignalsLeft.size()-1).getSignalPath();

            if (SignalsLeft.size()>1){
                for (int i=SignalsLeft.size()-1;i<SignalsLeft.size();i++){
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
            stabVelL=0;
        }
        else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementLeft, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementLeft, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementLeft, "aZ [m/s^2]");

            stabVelL = MovementProcessor.ComputeRegularity(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
        }
        return (float) stabVelL;
    }

    // Rigth Hand
    private float CalculateStabCirclesRight(){
        double stabVelR=0;

        CSVFileReader FileReader=new CSVFileReader(this);
        MovementProcessing MovementProcessor=new MovementProcessing();

        int IDEx=23;
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);


        SignalDataService signalDataService =new SignalDataService(this);
        List<SignalDA> SignalsRight=signalDataService.getSignalsbyname(name);
        if (SignalsRight.size()>0){
            path_movementRight=PATH+SignalsRight.get(SignalsRight.size()-1).getSignalPath();

            if (SignalsRight.size()>1){
                for (int i=SignalsRight.size()-1;i<SignalsRight.size();i++){
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
            stabVelR=0;
        }
        else {
            CSVFileReader.Signal TremorSignalaX = FileReader.ReadMovementSignal(path_movementRight, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY = FileReader.ReadMovementSignal(path_movementRight, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ = FileReader.ReadMovementSignal(path_movementRight, "aZ [m/s^2]");

            stabVelR = MovementProcessor.ComputeRegularity(TremorSignalaX.Signal, TremorSignalaY.Signal, TremorSignalaZ.Signal);
        }

        return (float) stabVelR;

    }
    //--------END FUNCTIONS FOR STABILITY---------------------------------------------------
}