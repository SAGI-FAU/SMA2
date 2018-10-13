package com.sma2.sma2.FeatureExtraction.Speech.features;

import com.sma2.sma2.FeatureExtraction.Speech.tools.sigproc;

import static java.util.Arrays.copyOfRange;

/**
 * Created by TOMAS on 20/04/2017.
 */

// This is a test comment.

public class BarkEnergy {
    //Sig is a frame window of the speech signal. The length must be power of 2.
    public BarkEnergy(){
    }

    //Compute Bark
    public  float[] BarkE(float sig[], int Fs){
        sigproc SigProc = new sigproc();
        //Apply hamming window to the signal
        float sig_fft[] = SigProc.makeWindow(sig,0);
        //Compute FFT
        sig_fft =   SigProc.signal_fft(sig_fft,Fs);
        int barkB = 17;
        float sig_bark[] = new float[barkB];
        //Spectrum frequencies to Bark scale
        int bark_idx[] = Bark_freq(sig.length,Fs);
        //Compute Bark band energies
        int k=0;
        for(int i=0;i<barkB;i++){
            int m = 0;
            for (int j=k;j<=bark_idx.length;j++) {
                if (bark_idx[j] == i){
                    sig_bark[i] = sig_bark[i] + sig_fft[j]*sig_fft[j];
                    m = m+1;
                }//END if
                else{
                    //-------------------------------------------------
                    //To return log(BBE) uncomment this:
                    sig_bark[i] = (float) Math.log(sig_bark[i]/m);
                    //-------------------------------------------------
                    //sig_bark[i] = sig_bark[i]/m;
                    k = j;
                    break;
                }
            }//END FOR j
        }//END FOR i
        float BBE[] = copyOfRange(sig_bark,1,sig_bark.length);
        return BBE;
        //return sig_bark;
    }

    //Estimate bark frequencies
    public  int[] Bark_freq(int N, int Fs){
        //N : length of frame signal
        //Fs: Sampling frequency
        float f[] = new float[(N/2)+1];
        int barkf[] = new int[(N/2)+1];
        for (int i=1;i<(N/2)+1;i++) {
            //Frequencies from the spectrum
            f[i] = f[i-1] + ((float) Fs / (float) N);
            //Bark scale frequencies
            barkf[i] = (int) Math.floor(13*Math.atan(0.00076*f[i])+3.5*Math.atan((f[i]/(float) 7500)*(f[i]/(float) 7500)));
        }
        return barkf;
    }

}