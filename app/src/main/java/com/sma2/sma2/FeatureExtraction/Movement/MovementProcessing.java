package com.sma2.sma2.FeatureExtraction.Movement;

import com.sma2.sma2.FeatureExtraction.Speech.tools.FFT;
import com.sma2.sma2.FeatureExtraction.Speech.tools.array_manipulation;
import com.sma2.sma2.FeatureExtraction.Speech.tools.sigproc;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;
import static java.util.Arrays.copyOfRange;

public class MovementProcessing {


    private float winlen = 3;//Default window length 40ms
    private float winstep = 0.5f;//Default window step 30ms;
    private int maxf0=50;
    private double minf0=0.3;
    private int inilag, endlag;
    private sigproc SG = new sigproc();
    private array_manipulation arrays=new array_manipulation();
    double low_th=0.03;
    float vocingthres = 0.45f;
    private FFT fft;
    private float unvocingthres = 0.3f;

    public MovementProcessing(){

    }


    public List<Double> RemoveGravity(List<Double> signal){

        double alpha = 0.8;
        double gravity=0.0;
        List<Double> SignalOut=new ArrayList<>();
        for (int j=0;j<signal.size();j++){
            gravity=alpha * gravity + (1 - alpha) * signal.get(j);
            SignalOut.add(signal.get(j)- gravity);
        }
        return SignalOut;
    }


    public Double ComputePower (List<Double> signal){

        Double Power=0.0;
        for (int j=0;j<signal.size();j++){
            Power+=Math.pow(signal.get(j),2);
        }
        return Power/(double)signal.size();
    }


    public List<Double> getAccR(List<Double> accX, List<Double> accY, List<Double> accZ){
        List<Double> accR=new ArrayList<>();
        double accxi, accyi, acczi;
        for (int j=0;j<accX.size();j++){
            accxi=Math.pow(accX.get(j),2);
            accyi=Math.pow(accY.get(j),2);
            acczi=Math.pow(accZ.get(j),2);
            accR.add(Math.sqrt(accxi+accyi+acczi));
        }
        return accR;
    }

    public Double ComputeTremor(List<Double> AccX, List<Double> AccY, List<Double> AccZ){


        List<Double> AccXn, AccYn, AccZn, AccR;

        AccXn=RemoveGravity(AccX);
        AccYn=RemoveGravity(AccY);
        AccZn=RemoveGravity(AccZ);

        AccR=getAccR(AccXn, AccYn, AccZn);

        return 100/(1+ComputePower(AccR));


    }


    public float[] ComputeF0(List<Double> AccX, List<Double> AccY, List<Double> AccZ, int fs){
        List<Double> AccXn, AccYn, AccZn, AccR;

        inilag = (int) Math.ceil(fs/maxf0);//lower bound lag
        endlag  = (int) Math.ceil(fs/minf0);//upper bound lag
        AccXn=RemoveGravity(AccX);
        AccYn=RemoveGravity(AccY);
        AccZn=RemoveGravity(AccZ);

        AccR=getAccR(AccXn, AccYn, AccZn);

        //This function normalize the speech signal.

        //Get f0 contour from signal
        //Log.e("F0", "Inicia");
        float[] pitchC = f0_cont(AccR, fs);

        //Log.e("F0", "Listo");

        //Fix contour.
        pitchC = fixf0(pitchC);

        //Set pitch zero for windows less than the analysis window
        pitchC = zerof0(pitchC);

        //Interpolate missing values
        pitchC = interpf0(pitchC);


        //Voiced
        //List VoicedSeg = voiced(pitchC,sig, (int) (winstep*fs));

        return pitchC;
    }



    //F0 contour
    private float[] f0_cont(List<Double> signal, int fs) {



        double[] signal2=arrays.dlisttoarray(signal);
        //Find global absolute peak
        double[] temp = Arrays.copyOfRange(signal2, 0, signal2.length - 1);
        Arrays.sort(temp);
        double GAP = temp[temp.length - 1];
        GAP = Math.abs(GAP);//Global Absolute Peak
        double GAP2 = temp[0];
        GAP2 = Math.abs(GAP2);//in case the maximum is negative
        if (GAP < GAP2) {
            GAP = GAP2;
        }

        //Frame speech signal
        int numberOfsamples = (int) Math.ceil(winlen * fs);
        //Window step
        int windowshift = (int) Math.ceil(winstep * fs);
        int ini_frame = 0, end_frame = numberOfsamples;
        //Number of frames to analyze
        int numberOfframes;
        if (windowshift == 0) { //if there is no window shift
            numberOfframes = (int) (Math.floor(signal2.length / numberOfsamples));
            windowshift = numberOfframes;
        } else {
            float temp2 = winstep / winlen;
            numberOfframes = (int) (Math.floor((signal2.length / numberOfsamples) / temp2));
        }
        double sig_frame[] = copyOfRange(signal2, ini_frame, end_frame);
        //-----------------------------------------------------------------------------------------
        //Compute ACF using FFT
        int zpad = 2 * sig_frame.length;
        //Append half a window length of zeros
        while ((zpad & (zpad - 1)) != 0)//Append zeros until the number of samples is a power of two
        {
            zpad = zpad + 1;
        }
        fft = new FFT(zpad, fs);
        //Calculate ACF for hanning window. This is used to normalize the ACF (r_sig/r_win)
        double[] winfun = new double[zpad];
        Arrays.fill(winfun, 1);
        winfun = SG.makeWindow(winfun, 1);
        double[] winRx = Arrays.copyOfRange(winfun, (int) Math.ceil(((winfun.length) / 2)), winfun.length);
        winRx = Arrays.copyOfRange(winRx,inilag,endlag);
        //-----------------------------------------------------------------------------------------
        float[] f0c = new float[numberOfframes];
        double LAP, LAP2;
        for (int i = 0; i < numberOfframes; i++) {
            sig_frame = copyOfRange(signal2, ini_frame, end_frame);
            //Voiceless candidate
            temp = copyOfRange(sig_frame, 0, sig_frame.length);//it is necessary
            Arrays.sort(temp);
            LAP = temp[temp.length - 1];//Local Absolute Peak
            LAP = Math.abs(LAP);
            LAP2 = temp[0];
            LAP2 = Math.abs(LAP2);//in case that the maximum is negative
            float[] sig_framef =new float[sig_frame.length];
            if (LAP < LAP2) {
                LAP = LAP2;
            }
            if (LAP < (low_th * GAP)) {
                f0c[i] = 0f;
            } else {
                //Estimate pitch for each speech segment

                for (int j = 0 ; j < sig_frame.length; j++)
                {
                    sig_framef[j] = (float) sig_frame[j];
                }


                f0c[i] = f0_acf(  sig_framef, winRx, fs);
            }
            ini_frame = ini_frame + windowshift;
            end_frame = end_frame + windowshift;
        }
        return f0c;
    }


    //Calculated pitch according to Paul Boersma method(Praat)
    private float f0_acf(float[] sig_frame, double[] winRx, int fs) {

        //Apply hanning window to speech signal
        float[] Hannsig = SG.makeWindow(sig_frame, 1);
        //Calculate autocorrelation
        float[] sigRx = fft.acf(Hannsig);
        //float[] sigRx = new float[sig_frame.length];
        //acf(Hannsig,sigRx);
        float f0 = 0;
        if (sigRx[0] > vocingthres) {

            //Normalize ACF.
            float maxACF = sigRx[0];//The highest value of the ACF is at lag=0
            //Only consider lags (in the ACF) between fs/maxf0 and fs/minf0, which corresponds
            // to the range of pitch values to detect.
            sigRx = Arrays.copyOfRange(sigRx,inilag,endlag);
            float[] NRx = new float[sigRx.length];
            for (int i = 0; i < NRx.length; i++) {
                //(sigRx[i]/maxACF) is used to ensure that sigRx values range between 1 and -1
                NRx[i] = (sigRx[i]/maxACF) / (float) winRx[i];
            }
            //Find Max
            float[] temp = Arrays.copyOfRange(NRx,0, NRx.length);
            Arrays.sort(temp);
            float Mc = temp[temp.length - 1];
            if (Mc >= unvocingthres)//Avoid unvoiced seg
            {
                int lag = 1;
                for (int i = 0; i < NRx.length; i++) {
                    if (NRx[i] == Mc) {
                        lag = i;
                    }
                }
                //Calculate f0
                if (lag != 0) {
                    f0 = (float) fs / ((float) (lag + inilag));
                }
            }
        }
        return f0;
    }


    //Fix contour
    //Unusual variations in pitch in signals.
    //cont: f0cont
    private float[] fixf0(float[] cont) {

        float[] d = arrays.diff(cont);
        float[] d_abs = arrays.absArr(d);//Absolute value of differences
        List df0 = arrays.find(d_abs, 10, 2);//Find pitch variations greater than 10Hz
        if (df0.size()>0) {

            for (int i = 0; i < df0.size(); i++) {
                int ind = (int) df0.get(i);
                cont[ind+1] = 0;
            }}


        return cont;
    }



    //Set f0 to zero in frames with a length less than the analysis window
    private float[] zerof0(float[] cont) {
        boolean initzero = false;
        int posIni = 0;
        int posfin;
        for (int i = 1; i < cont.length-1; i++) {
            if ((cont[i] != 0) && (cont[i - 1] == 0)) {
                initzero = true;
                posIni = i;
            }
            if (initzero) {
                if ((cont[i] != 0) && (cont[i + 1] == 0)) {
                    posfin = i;
                    int diff = Math.abs(posfin - posIni)+1;
                    int minseg = 4;//Minimum number of windows considered as unvoiced
                    if (diff < minseg) {//If f0=0 in a non-unvoiced nor silence segment, then interpolate.
                        for (int j = 0; j <= diff; j++) {
                            cont[posIni + j] = 0;
                        }
                        initzero = false;
                    }
                }
            }
        }
        return  cont;
    }


    //Interpolate missing f0
    //cont: f0cont
    private float[] interpf0(float[] cont) {
        boolean initzero = false;
        int posIni = 0;
        int posfin;
        for (int i = 1; i < cont.length-1; i++) {
            if ((cont[i] == 0) && (cont[i - 1] != 0)) {
                initzero = true;
                posIni = i - 1;
            }

            if (initzero) {
                if ((cont[i] == 0) && (cont[i + 1] != 0)) {
                    posfin = i + 1;


                    int diff = Math.abs(posfin - posIni);
                    int minseg = 5;//Minimum number of windows considered for intepolation
                    if (diff <= minseg) {//If f0=0 in a non-unvoiced nor silence segment, then interpolate.
                        float[] f0intp = arrays.interpolate(cont[posIni], cont[posfin], diff);
                        for (int j = 0; j <= diff; j++) {
                            cont[posIni + j] = f0intp[j];
                        }
                        initzero = false;
                    }
                }
            }
        }
        return  cont;
    }


    public float perc_stab_freq(float[] f0) {
        //Ensure nonzero values in f0 contour
        List lf0 = arrays.find(f0,0f,2);
        //Convert list to float array
        //List to array
        float[] newF0 = new float[lf0.size()];
        for(int i=0;i<lf0.size();i++)
        {
            newF0[i] = f0[(int)lf0.get(i)];
        }
        f0 = newF0;
        int N = f0.length;
        if (N<4){
            return 100f;
        }
        //Find Max
        float[] temp = Arrays.copyOfRange(f0,0, f0.length);
        Arrays.sort(temp);
        float Mp = temp[temp.length - 1];//Maximum pitch
        //Array with variations between elements of array
        float jitt = 0f;
        float avgjitt=0f;
        int nf=0;
        for (int i = 0; i < N-3; i++)
        {
            jitt=0;
            for (int j=i;j<i+3;j++){
                jitt+= abs(f0[j] - Mp);
            }
            avgjitt+= (100*jitt)/(N*Mp);
            nf++;

        }
        return avgjitt/nf;
    }


}
