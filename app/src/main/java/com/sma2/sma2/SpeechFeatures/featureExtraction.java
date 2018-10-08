package com.sma2.sma2.SpeechFeatures;

import com.sma2.sma2.SpeechFeatures.features.BarkEnergy;
import com.sma2.sma2.SpeechFeatures.features.MFCC;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.copyOfRange;

/**
 * Created by TOMAS on 10/11/2016.
 */

public class featureExtraction {
    protected int NumberOfFeats=0;

    public featureExtraction()
    {
        int MFCCs = 13;
        int BBEs = 16;
        NumberOfFeats = MFCCs+BBEs;
    }

    public List<float[]> feat_ext(float[] signal, int Fs) {
        //-----------------------------------------------------------------------------------------
        //Frame speech signal
        //----------------------------------------------------------------------------------------
        //Percentage of window shift (%p/100)
        double shiftperce = 0.5;
        //numberOfsamples must be power of 2 to compute Bark Energies and FFT
        int numberOfsamples = 512, windowshift = (int) Math.ceil(numberOfsamples * shiftperce);
        int ini_frame = 0, end_frame = numberOfsamples;

        //Number of frames to analyze
        int numberOfframes;
        if (shiftperce == 0) { //if there is no window shift
            numberOfframes = (int) (Math.floor(signal.length / numberOfsamples));
            windowshift = numberOfframes;
        } else {
            numberOfframes = (int) (Math.floor(signal.length / numberOfsamples) / shiftperce);
        }
        List<float[]> featvec = new ArrayList<float[]>();
        for (int i = 0; i < numberOfframes; i++) {
            float sig_frame[] = copyOfRange(signal, ini_frame, end_frame);
            //----------------------------------------------------------
            //Compute MFCC
            MFCC coef = new MFCC(numberOfsamples,13,26,Fs);
            float[] mfccoef = coef.getMFCC(sig_frame);
            //mfcc_signal.add(mfccoef);
            //----------------------------------------------------------
            //Compute log Bark band energies
            BarkEnergy bbe = new BarkEnergy();
            float bbenergy[] = bbe.BarkE(sig_frame, Fs);
            //bbe_signal.add(bbenergy);
            //----------------------------------------------------------
            float[] temp = mergefeats(mfccoef,bbenergy);
            featvec.add(temp);
            //new frame
            ini_frame = ini_frame + windowshift;
            end_frame = end_frame + windowshift;
        }
        //END of frame signal
        //----------------------------------------------------------------------------------------

        return featvec;
    }

    public int getNumberOfFeatures()
    {
        return NumberOfFeats;
    }

    private float[] mergefeats(float[] featvec1,float[] featvec2)
    {
        int n =featvec1.length+featvec2.length;
        float[] merge = new float[n];
        for(int i=0;i<featvec1.length;i++) {
                merge[i] = featvec1[i];
            }
        int fv = 0;
        for (int j = featvec1.length; j < featvec1.length+featvec2.length; j++) {

            merge[j] = featvec2[fv];
            fv = fv+1;
        }
        return merge;
    }

    //Estimate mean and standard deviation from array of Bark energies. TO DO
    public float[] errorBark(List<float[]> feats ){
        float BBEm[] = new float[16];
        float BBEs[] = new float[16];
        for (int i=0;i<BBEm.length;i++){
            for (int j=0;j<feats.size();j++) {
                float temp[] = feats.get(j);
                BBEm[i] = BBEm[i] + temp[i];
            }
            BBEm[i] = BBEm[i]/feats.size();
        }
        return BBEm;
    }

}
