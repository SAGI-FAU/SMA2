package com.sma2.sma2;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.sma2.sma2.SpeechFeatures.features.phon_feats;
import com.sma2.sma2.SpeechFeatures.features.Energy;
import com.sma2.sma2.SpeechFeatures.tools.TransitionDectector;
import com.sma2.sma2.SpeechFeatures.tools.WAVfileReader;
import com.sma2.sma2.SpeechFeatures.tools.array_manipulation;
import com.sma2.sma2.SpeechFeatures.tools.f0detector;
import com.sma2.sma2.SpeechFeatures.tools.sigproc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Start_results_Activity extends AppCompatActivity{
    private String WAVpath = null;
    private WAVfileReader WAVR = new WAVfileReader();
    private sigproc SG = new sigproc();
    private f0detector f0meth = new f0detector();
    private phon_feats PF = new phon_feats();
    private float signal[];
    private int Fs;
    private float[] f0;
    array_manipulation array_manipulation = new array_manipulation();

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
        //float fluency = PF.fluency_cal(f0);
        TransitionDectector transitionDectector = new TransitionDectector();
        ArrayList<ArrayList<Integer>> data = transitionDectector.detect(f0);
        ArrayList<Integer> index_array_onset = data.get(0);
        ArrayList<Integer> index_array_offset = data.get(1);


        int start;
        int end;
        start = (int) Math.ceil(index_array_onset.get(0)*0.02*Fs);
        end = (int) Math.round(index_array_offset.get(0)*0.02*Fs);
        int len = end - start;
        float[] voice_signal = Arrays.copyOfRange(signal,start,end);

        for(int i = 1; i < index_array_onset.size();i++){
            start = (int) Math.ceil(index_array_onset.get(i)*0.02*Fs);
            end = (int) Math.round(index_array_offset.get(i)*0.02*Fs);
            len+=(end - start);
            float[] temp = Arrays.copyOfRange(signal,start,end);
            float[] temp2 = new float[len];
            System.arraycopy(voice_signal,0,temp2,0,voice_signal.length);
            System.arraycopy(temp,0,temp2,voice_signal.length,temp.length);
            voice_signal = temp2;
        }

        List voice_element = array_manipulation.find(f0,0,2);

        Energy energy = new Energy();
        float[] energy_contour = energy.energyContour(voice_signal,Fs);
        float per_energy = energy.perturbationEnergy(energy_contour);
        jitter_cal();
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
