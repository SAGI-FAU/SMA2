package com.sma2.sma2.FeatureExtraction.Speech;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
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
import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;
import com.sma2.sma2.FeatureExtraction.Speech.features.PhonFeatures;
import com.sma2.sma2.FeatureExtraction.Speech.tools.WAVfileReader;
import com.sma2.sma2.FeatureExtraction.Speech.tools.f0detector;
import com.sma2.sma2.FeatureExtraction.Speech.tools.sigproc;
import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
        List<float[]> Voiced=F0Detector.voiced(F0, Signal);

        List<Float> Duration=new ArrayList<>();
        float[] segment;
        for(int i=0;i<Voiced.size();i++){
            segment=Voiced.get(i);
            Duration.add(((float)segment.length*1000)/InfoSig[0]);
        }

        return PhonFeatures.calculateSD(Duration);

    }


    private void PhonationFeatures() {
        // get exercises from sustained vowel ah to compute phonation features
        SignalDataService signalDataService = new SignalDataService(this);
        DecimalFormat df = new DecimalFormat("#.00");

        long N = signalDataService.countSignalsbyname("A");

        if (N>0) {

            List<SignalDA> signals = signalDataService.getSignalsbyname("A");
            if (signals.size() > 0) {
                path_ah = signals.get(signals.size() - 1).getSignalPath();
                if (signals.size() > 5) {
                    for (int i = 4; i >= 0; i--) {
                        path_ah_all.add(signals.get(i).getSignalPath());
                    }
                } else {
                    for (int i = signals.size() - 1; i >= 0; i--) {
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

        int j;
        float JitterTemp;
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
        if (path_ah_all.size() > 0) {
            j = path_ah_all.size() - 1;
            for (int i = 0; i < path_ah_all.size(); i++) {
                JitterTemp = Jitter(path_ah_all.get(j));
                series.appendData(new DataPoint(i + 1, JitterTemp), true, 6);
                j = j - 1;
            }

        } else {
            series.appendData(new DataPoint(1, 0), true, 5);
        }


        GraphView graph = findViewById(R.id.bar_perc_phonation);
        graph.addSeries(series);

        // styling

        series.setColor(Color.rgb(255, 140, 0));

        series.setSpacing(5);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(6);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        series.setTitle(getResources().getString(R.string.jitter));
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(getResources().getString(R.string.session));
        gridLabel.setVerticalAxisTitle(getResources().getString(R.string.jitter));

    }



    private void ArticulationFeatures(){
        SignalDataService signalDataService = new SignalDataService(this);
        DecimalFormat df = new DecimalFormat("#.00");
        long N = signalDataService.countSignalsbyname("Pataka");

        if (N>0) {
            List<SignalDA> signals = signalDataService.getSignalsbyname("Pataka");
            if (signals.size() > 0) {
                path_pataka = signals.get(signals.size() - 1).getSignalPath();
                if (signals.size() > 5) {
                    for (int i = 4; i >= 0; i--) {
                        path_pataka_all.add(signals.get(i).getSignalPath());
                    }
                } else {
                    for (int i = signals.size() - 1; i >= 0; i--) {
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


        int j;
        float DDK_regTemp;
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
        if (path_pataka_all.size() > 0) {
            j = path_pataka_all.size() - 1;
            for (int i = 0; i < path_pataka_all.size(); i++) {
                DDK_regTemp = DDKRegularity(path_pataka_all.get(j));
                series.appendData(new DataPoint(i + 1, DDK_regTemp), true, 6);
                j = j - 1;
            }

        } else {
            series.appendData(new DataPoint(1, 0), true, 5);
        }


        GraphView graph = findViewById(R.id.bar_perc_articulation);
        graph.addSeries(series);

        // styling

        series.setColor(Color.rgb(255, 140, 0));

        series.setSpacing(5);
        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(6);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        series.setTitle(getResources().getString(R.string.ddk_reg));
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle(getResources().getString(R.string.session));
        gridLabel.setVerticalAxisTitle(getResources().getString(R.string.ddk_reg));


    }

}
