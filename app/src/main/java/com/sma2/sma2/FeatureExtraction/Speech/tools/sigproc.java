package com.sma2.sma2.FeatureExtraction.Speech.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.copyOfRange;

/**
 * Created by TOMAS on 18/02/2017.
 * Modified on 08/10/2018  for SMA2 - Tomas
 *
 * This file includes the following pre-processing procedures:
 * - Signal normalization (method: normsig).
 * - Mean of array (method: meanval). Private method.
 * - Windowing: Hamming and Hanning (method: makeWindow).
 * - Signal power spectrum (method: signal_fft).
 * - Framing (method: sigframe).
 */

public class sigproc {
    public sigproc(){
    }

    /**
     * Signal normalization
     * Re-scales and remove DC level from a signal (time-series). The amplitude of the signal is
     * re-scaled between -1 and 1.
     * @param sig - Signal to normalize
     * @return Array containing the normalized signal
     */
    public float[] normsig(float sig[])
    {
        float msig = meanval(sig);
        //Remove DC
        float[] Nsig = new float[sig.length];
        for (int i=0;i<sig.length;i++)
        {
            Nsig[i] = sig[i]-msig;
        }
        Nsig = sig;

        //Scale between -1 and 1
        float[] temp = Arrays.copyOf(Nsig,Nsig.length);
           Arrays.sort(temp);
        float max = temp[temp.length-1];
        max = Math.abs(max);
        float min = temp[0];
        min = Math.abs(min);
        if (max<min)
        {
           max = min;
        }

        //Re-scale signal
        for (int i=0;i<Nsig.length;i++)
        {
            Nsig[i] = Nsig[i]/max;
        }
        return Nsig;
    }

    /**
     * Mean of array
     * Computes mean value from array (Signal). Used to remove DC level.
     * @param sig - Array with the signal.
     * @return Integer with the mean value of the array.
     */
    public float meanval(float[] sig)
    {
        //Find DC level
        float msig=0;
        for (float aSig : sig) {
            msig = msig + aSig;
        }
        msig = msig/sig.length;
        return msig;
    }


    public static float calculatemean(float[] m) {
        float sum = 0;
        for (int i = 0; i < m.length; i++) {
            sum += m[i];
        }
        return sum / m.length;
    }

    public static float calculateSD(float[] numArray)
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;
        for(double num : numArray) {
            sum += num;
        }
        double mean = sum/length;
        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        return (float) Math.sqrt(standardDeviation/length);
    }



    /**
     * Windowing
     * Applies windowing to the signal. The current method implements Hanning and Hamming windowing.
     * @param win_sig - Short-time frame from the signal
     * @param selwin - Flag used to select between hamming (selwin=0) or hanning (selwin=1) windowing
     * @return Integer with the mean value of the array.
     */
    public float[] makeWindow(float win_sig[],int selwin) {

        float[] window = new float[win_sig.length];
        int n = win_sig.length;
        for(int i = 0; i < window.length; i++)
            if (selwin==0) {//Make a hamming window
                window[i] = win_sig[i] * ((float) (0.54 - 0.46 * Math.cos(2 * Math.PI * i / (n - 1))));
            }
            else if(selwin==1) {//Make hanning window
                window[i] = win_sig[i] * ((float) (0.5 - 0.5 * Math.cos(2 * Math.PI * i / (n - 1))));
            }
        return window;
    }



    /**
     * Windowing
     * Applies windowing to the signal. The current method implements Hanning and Hamming windowing.
     * @param win_sig - Short-time frame from the signal
     * @param selwin - Flag used to select between hamming (selwin=0) or hanning (selwin=1) windowing
     * @return Integer with the mean value of the array.
     */
    public double[] makeWindow(double win_sig[],int selwin) {

        double[] window = new double[win_sig.length];
        int n = win_sig.length;
        for(int i = 0; i < window.length; i++)
            if (selwin==0) {//Make a hamming window
                window[i] = win_sig[i] * ((0.54 - 0.46 * Math.cos(2 * Math.PI * i / (n - 1))));
            }
            else if(selwin==1) {//Make hanning window
                window[i] = win_sig[i] * ( (0.5 - 0.5 * Math.cos(2 * Math.PI * i / (n - 1))));
            }
        return window;
    }


    /**
     * Power spectrum
     * Computes the power spectrum of a speech signal.
     * @param win_sig - Short-time frame extracted from the signal.
     * @param Fs - Sampling frequency.
     * @return Array with the power spectrum of the signal.
     */
    public float[] signal_fft(float[] win_sig,int Fs)
    {
       //signal frame with window function applied
       FFT fft = new FFT(win_sig.length,Fs);
       fft.forward(win_sig);
        float[] sig_spec = new float[fft.specSize()];
        //Get spectrum of the signal (amplitudes)
        for (int i =0;i<fft.specSize();i++) {
            sig_spec[i] = fft.getBand(i);
        }
        return sig_spec;
    }

    /***
     * Frame speech signal
     * @param signal - speech signal
     * @param fs - sampling frequency of the speech signal
     * @param winlen - size of the analysis window measured in SECONDS (example:0.03)
     * @param winstep - size of the steps of the analysis windows measured in SECONDS
     * @return List with the framed speech signal
     */
    public List<float[]> sigframe(float[] signal,int fs,float winlen, float winstep) {
        int numberOfsamples = (int) Math.ceil(winlen * fs);
        //Window step
        int windowshift = (int) Math.ceil(winstep * fs);
        int ini_frame = 0, end_frame = numberOfsamples;
        //Number of frames to analyze
        int numberOfframes;
        if (windowshift == 0) { //if there is no window shift
            numberOfframes = (int) (Math.floor(signal.length / numberOfsamples));
            windowshift = numberOfframes;
        } else {
            float temp = winstep / winlen;
            numberOfframes = (int) (Math.floor((signal.length / numberOfsamples) / temp));
        }
        List<float[]> frames = new ArrayList<>();
        for (int i = 0; i < numberOfframes; i++) {
            float sig_frame[] = copyOfRange(signal, ini_frame, end_frame);
            //new frame
            frames.add(sig_frame);
            ini_frame = ini_frame + windowshift;
            end_frame = end_frame + windowshift;
        }
        return frames;
    }

}