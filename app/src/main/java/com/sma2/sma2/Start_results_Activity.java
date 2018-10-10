package com.sma2.sma2;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sma2.sma2.SpeechFeatures.features.phon_feats;
import com.sma2.sma2.SpeechFeatures.tools.WAVfileReader;
import com.sma2.sma2.SpeechFeatures.tools.f0detector;
import com.sma2.sma2.SpeechFeatures.tools.sigproc;

import java.io.File;

public class Start_results_Activity extends AppCompatActivity{
    private String WAVpath = null;
    private WAVfileReader WAVR = new WAVfileReader();
    private sigproc SG = new sigproc();
    private f0detector f0meth = new f0detector();
    private phon_feats PF = new phon_feats();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.res_test);

        jitter_cal();
    }

    public void jitter_cal()
    {
        //WAV file path
        WAVpath = Environment.getExternalStorageDirectory() + File.separator + "AppSpeechData"+ File.separator +"WAV"+ File.separator +"patvowel.wav";

        //Get number of samples (infosig[0]) and sampling frequency (infosig[1])
        int infosig[] = WAVR.getdatainfo(WAVpath), Fs = infosig[1];

        //Read WAV audio file (as a float array)
        float signal[] = WAVR.readWAV(infosig[0]);

        //Normalize signal
        signal = SG.normsig(signal);

        //Extract f0 contour
        float[] f0 = f0meth.sig_f0(signal,Fs,0.04f,0.02f);

        //jitter
        float jitt = PF.jitter(f0);

        //Display data info
        TextView textView = findViewById(R.id.featname);
        //File name
        textView.setText(String.valueOf(jitt));
    }

}
