package com.sma2.sma2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sma2.sma2.SpeechFeatures.features.phon_feats;
import com.sma2.sma2.SpeechFeatures.tools.WAVfileReader;
import com.sma2.sma2.SpeechFeatures.tools.f0detector;
import com.sma2.sma2.SpeechFeatures.tools.sigproc;

public class Start_results_Activity extends AppCompatActivity{
    private WAVfileReader WAVR = new WAVfileReader();//WAV reader
    private sigproc SG = new sigproc();//Preprocessing
    private f0detector f0meth = new f0detector();//Pitch contour
    private phon_feats PF = new phon_feats();//Phonation features

    private String WAVpath = null;//WAV files
    private int Fs = 0;
    private String ACCpath = null;//Accelerometer files
    private String TAPpath = null;//Tapping files

    public Start_results_Activity(String WAVpath, String ACCpath, String TAPpath){
        WAVpath = WAVpath;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        //TODO: Create the layout to display the features (activity_results)
        //TODO: Compute speech/movement features and display them in the screen.
        //TODO: Store features in the database.

    }

    /***
     * Read wav file and get the speech signals and the sampling frequency
     * @return Array with the normalized speech signal.
     * */
    private float[] get_speech_signal()
    {
        //Get number of samples (infosig[0]) and sampling frequency (infosig[1])
        int infosig[] = WAVR.getdatainfo(WAVpath), Fs = infosig[1];

        //Read WAV audio file (as a float array)
        float signal[] = WAVR.readWAV(infosig[0]);

        //Normalize signal
        signal = SG.normsig(signal);

        return signal;
    }

}
