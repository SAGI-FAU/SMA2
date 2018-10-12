package com.sma2.sma2;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.sma2.sma2.SpeechFeatures.features.phon_feats;
import com.sma2.sma2.SpeechFeatures.features.Energy;
import com.sma2.sma2.SpeechFeatures.tools.WAVfileReader;
import com.sma2.sma2.SpeechFeatures.tools.f0detector;
import com.sma2.sma2.SpeechFeatures.tools.sigproc;

import java.io.File;
import java.util.ArrayList;

public class Start_results_Activity extends AppCompatActivity{
    private String WAVpath = null;
    private WAVfileReader WAVR = new WAVfileReader();
    private sigproc SG = new sigproc();
    private f0detector f0meth = new f0detector();
    private phon_feats PF = new phon_feats();
    private float signal[];
    private int Fs;
    float[] f0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.res_test);

        //WAV file path
        WAVpath = Environment.getExternalStorageDirectory() + File.separator + "Music" + File.separator + "aud.wav";

        //Get number of samples (infosig[0]) and sampling frequency (infosig[1])
        int infosig[] = WAVR.getdatainfo(WAVpath);
        Fs = infosig[1];
        //Read WAV audio file (as a float array)
        signal = WAVR.readWAV(infosig[0]);

        //Normalize signal
        signal = SG.normsig(signal);

        //Extract f0 contour
        f0 = f0meth.sig_f0(signal, Fs, 0.04f, 0.02f);

        //jitter_cal();
        //float fluency = fluency_cal();
        Energy energy = new Energy();
        float[] energy_contour = energy.energyContour(signal,Fs);
        float per_energy = energy.perturbationEnergy(energy_contour);
        Log.d("Prueba","Value "+per_energy);
    }

    private float fluency_cal() {
        ArrayList<Integer> index_array_onset = new ArrayList<Integer>();
        ArrayList<Integer> index_array_offset = new ArrayList<Integer>();
//        int start;
//        int end;
        for(int i = 1;i < f0.length;i++){
            int round_1 = (int) Math.round(f0[i-1]);
            int round = (int) Math.round(f0[i]);
            if((round_1 == 0) && (round > 0)){
//                start = (int) Math.round(i*0.02*Fs - 0.08*Fs);
//                end = (int) Math.ceil(i*0.02*Fs + 0.08*Fs);
//                float[] transition_frame = Arrays.copyOfRange(signal,start,end);
//                sigproc sigprocObject = new sigproc();
//                List<float[]> dataFrame = sigprocObject.sigframe(transition_frame,Fs,0.04f,0.02f);
                index_array_onset.add(i);
            }else if((round_1 > 0) && (round == 0)){
//                start = (int) Math.ceil(i*0.02*Fs - 0.08*Fs);
//                end = (int) Math.ceil(i*0.02*Fs + 0.08*Fs);
//                float[] transition_frame = Arrays.copyOfRange(signal,start,end);
//                sigproc sigprocObject = new sigproc();
//                List<float[]> dataFrame = sigprocObject.sigframe(transition_frame,Fs,0.04f,0.02f);
                index_array_offset.add(i);
            }
        }
        if(index_array_offset.get(0) < index_array_onset.get(0)){
            index_array_onset.add(0,0);
        }
        if(index_array_offset.get(index_array_offset.size()-1) <  index_array_onset.get(index_array_onset.size()-1)){
            index_array_offset.add(f0.length);
        }

        ArrayList<Float> voiced_time = new ArrayList<Float>();
        float duration;
        float mean=0;
        for(int i = 0; i < index_array_onset.size(); i++){
            duration = (index_array_offset.get(i) - index_array_onset.get(i))*0.02f;
            mean+=duration;
            voiced_time.add(duration);
        }
        mean/=voiced_time.size();
        return mean;
    }

    public void jitter_cal() {
        //jitter
        float jitt = PF.jitter(f0);
        //Display data info
        TextView textView = findViewById(R.id.featname);
        //File name
        textView.setText(String.valueOf(jitt));
    }
}
