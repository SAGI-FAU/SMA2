package com.sma2.sma2.FeatureExtraction.Speech;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;
import com.sma2.sma2.FeatureExtraction.GetExercises;
import com.sma2.sma2.FeatureExtraction.GraphManager;
import com.sma2.sma2.FeatureExtraction.Speech.features.PhonFeatures;
import com.sma2.sma2.FeatureExtraction.Speech.tools.WAVfileReader;
import com.sma2.sma2.FeatureExtraction.Speech.tools.f0detector;
import com.sma2.sma2.FeatureExtraction.Speech.tools.sigproc;
import com.sma2.sma2.MainActivity;
import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Speech_features_Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView tjitter, tmessage_phonation, tddk_reg, tmessage_articulation;
    private ImageView iEmojinPhonation, iEmojinArticulation;
    private Button bBack;
    private String path_ah = null;
    private List<String> path_ah_all= new ArrayList<>();
    private String path_pataka = null;
    private List<String> path_pataka_all= new ArrayList<>();
    private float jitter, DDK_reg;
    private List<Float> JitterAll=new ArrayList<>();

    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/AUDIO/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_features);
        bBack=findViewById(R.id.button_back_speech);
        tjitter=findViewById(R.id.tjitter);
        tddk_reg=findViewById(R.id.tddk_reg);
        tmessage_phonation=findViewById(R.id.tmessage_phonation);
        tmessage_articulation=findViewById(R.id.tmessage_articulation);

        iEmojinPhonation=findViewById(R.id.iEmojin_phonation);
        iEmojinArticulation=findViewById(R.id.iEmojin_articulation);

        SetListeners();

        PhonationFeatures();
        ArticulationFeatures();

    }


    private void SetListeners(){
        bBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button_back_speech:
                onButtonBack();
                break;
        }
    }

    private void onButtonBack(){
        Intent i =new Intent(Speech_features_Activity.this, MainActivityMenu.class);
        startActivity(i);

    }


    private float Jitter(String AudioFile){
        WAVfileReader wavFileReader=new WAVfileReader();
        f0detector F0Detector =new f0detector();
        PhonFeatures PhonFeatures=new PhonFeatures();

        int[]  InfoSig= wavFileReader.getdatainfo(AudioFile);
        float[] SignalAh=wavFileReader.readWAV(InfoSig[0]);
        //Normalize signal
        sigproc SigProc = new sigproc();
        SignalAh = SigProc.normsig(SignalAh);
        float[] F0= F0Detector.sig_f0(SignalAh, InfoSig[1]);
        return PhonFeatures.jitter(F0);
    }


    private float DDKRegularity(String AudioFile){
        WAVfileReader wavFileReader=new WAVfileReader();
        f0detector F0Detector =new f0detector();
        PhonFeatures PhonFeatures=new PhonFeatures();

        int[] InfoSig= wavFileReader.getdatainfo(AudioFile);
        float[] Signal=wavFileReader.readWAV(InfoSig[0]);
        sigproc SigProc = new sigproc();
        Signal = SigProc.normsig(Signal);
        float[] F0= F0Detector.sig_f0(Signal, InfoSig[1]);
        float sumF0=0;
        for (int i=0;i<F0.length;i++){
            sumF0+=F0[i];
        }
        if (sumF0==0){
            return 0;
        }
        else{
            List<float[]> Voiced=F0Detector.voiced(F0, Signal);

            List<Float> Duration=new ArrayList<>();
            float[] segment;
            for(int i=0;i<Voiced.size();i++){
                segment=Voiced.get(i);
                Duration.add(((float)segment.length*1000)/InfoSig[0]);
            }

            return PhonFeatures.calculateSD(Duration);
        }

    }


    private void PhonationFeatures() {
        // get exercises from sustained vowel ah to compute phonation features
        SignalDataService signalDataService = new SignalDataService(this);
        DecimalFormat df = new DecimalFormat("#.00");
        int IDEx=18;
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);

        long N = signalDataService.countSignalsbyname(name);

        if (N>0) {

            List<SignalDA> signals = signalDataService.getSignalsbyname(name);
            if (signals.size() > 0) {
                path_ah = signals.get(signals.size() - 1).getSignalPath();
                if (signals.size()>4){
                    for (int i=signals.size()-4;i<signals.size();i++){
                        path_ah_all.add(signals.get(i).getSignalPath());
                    }
                } else {
                    for (int i=0;i<signals.size();i++){
                        path_ah_all.add(signals.get(i).getSignalPath());
                    }
                }
            }
        }


        // compute phonation features for the last speech task from sustained vowel ah

        if (path_ah == null) {
            tjitter.setText(R.string.Empty);
        } else {
            jitter = Jitter(path_ah);
            String JitterStr = String.valueOf(df.format(jitter)) + "%";
            tjitter.setText(JitterStr);

        }

        // Emoji for phonation features

        if (jitter <= 3) {
            iEmojinPhonation.setImageResource(R.drawable.happy_emojin);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
            iEmojinPhonation.startAnimation(animation);
            tmessage_phonation.setText(R.string.Positive_message);
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
            tmessage_phonation.startAnimation(animation2);
        } else if (jitter <= 10) {
            iEmojinPhonation.setImageResource(R.drawable.medium_emojin);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
            iEmojinPhonation.startAnimation(animation);
            tmessage_phonation.setText(R.string.Medium_message);
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
            tmessage_phonation.startAnimation(animation2);
        } else {
            iEmojinPhonation.setImageResource(R.drawable.sad_emoji);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
            iEmojinPhonation.startAnimation(animation);
            tmessage_phonation.setText(R.string.Negative_message);
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
            tmessage_phonation.startAnimation(animation2);
        }

        float JitterTemp;






        GraphManager graphManager=new GraphManager(this);


        ArrayList<Integer> x=new ArrayList<>();
        ArrayList<Float> y=new ArrayList<>();
        for (int i=0;i<5;i++){

            if (i<path_ah_all.size()){
                JitterTemp = Jitter(path_ah_all.get(i));
                x.add(i+1);
                y.add(JitterTemp);
            }
            else{
                x.add(i+1);
                y.add((float) 0);
            }

        }

        String Title=getResources().getString(R.string.jitter);
        String Ylabel=getResources().getString(R.string.jitter);
        String Xlabel=getResources().getString(R.string.session);
        GraphView graph =findViewById(R.id.bar_perc_phonation);
        graphManager.BarGraph(graph, x, y, 0, 5, Title, Xlabel, Ylabel);






    }



    private void ArticulationFeatures(){
        int IDEx=11;
        GetExercises GetEx=new GetExercises(this);
        String name=GetEx.getNameExercise(IDEx);
        SignalDataService signalDataService = new SignalDataService(this);
        DecimalFormat df = new DecimalFormat("#.00");
        long N = signalDataService.countSignalsbyname(name);

        if (N>0) {
            List<SignalDA> signals = signalDataService.getSignalsbyname(name);
            if (signals.size() > 0) {
                path_pataka = signals.get(signals.size() - 1).getSignalPath();
                if (signals.size() > 4) {
                    for (int i=signals.size()-4;i<signals.size();i++){
                        path_pataka_all.add(signals.get(i).getSignalPath());
                    }
                } else {
                    for (int i=0;i<signals.size();i++){
                        path_pataka_all.add(signals.get(i).getSignalPath());
                    }
                }
            }
        }
        if (path_pataka == null) {
            tddk_reg.setText(R.string.Empty);
        } else {
            DDK_reg = DDKRegularity(path_pataka);
            String DDK_regStr = String.valueOf(df.format(DDK_reg)) + "ms";
            tddk_reg.setText(DDK_regStr);
        }

        // Emoji for articulation features

        if (DDK_reg <= 200) {
            iEmojinArticulation.setImageResource(R.drawable.happy_emojin);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
            iEmojinArticulation.startAnimation(animation);
            tmessage_articulation.setText(R.string.Positive_message);
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
            tmessage_articulation.startAnimation(animation2);
        } else if (DDK_reg <= 400) {
            iEmojinArticulation.setImageResource(R.drawable.medium_emojin);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
            iEmojinArticulation.startAnimation(animation);
            tmessage_articulation.setText(R.string.Medium_message);
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
            tmessage_articulation.startAnimation(animation2);
        } else {
            iEmojinArticulation.setImageResource(R.drawable.sad_emoji);

            Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoomin);
            iEmojinArticulation.startAnimation(animation);
            tmessage_articulation.setText(R.string.Negative_message);
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bounce);
            tmessage_articulation.startAnimation(animation2);
        }


        float DDK_regTemp;




        GraphManager graphManager=new GraphManager(this);


        ArrayList<Integer> x=new ArrayList<>();
        ArrayList<Float> y=new ArrayList<>();
        for (int i=0;i<5;i++){

            if (i<path_pataka_all.size()){
                DDK_regTemp = DDKRegularity(path_pataka_all.get(i));
                x.add(i+1);
                y.add(DDK_regTemp);
            }
            else{
                x.add(i+1);
                y.add((float) 0);
            }

        }

        String Title=getResources().getString(R.string.ddk_reg);
        String Ylabel=getResources().getString(R.string.ddk_reg);
        String Xlabel=getResources().getString(R.string.session);
        GraphView graph =findViewById(R.id.bar_perc_articulation);
        graphManager.BarGraph(graph, x, y, 0, 5, Title, Xlabel, Ylabel);




    }



}
