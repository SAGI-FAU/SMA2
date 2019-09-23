package com.sma2.sma2.FeatureExtraction.Speech.tools;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.copyOfRange;

/**
 * Created by TOMAS on 26/03/2017.
 */

public class f0detector {
    private float winlen;
    private float winstep;
    private int fs;
    private sigproc SG = new sigproc();
    private array_manipulation ArrM = new array_manipulation();
    private FFT fft;
    private int zpad;
    private float[] winRx;
    private int minf0=75;//Minimum pitch (default 75Hz)
    private int maxf0=500;//Maximum pitch (default 500Hz)
    private int inilag;
    private int endlag;
    private int maxcandi = 15;//Max number of candidates
    private float silencetrehs = 0.03f;//Silence threshold
    private float vocingthres = 0.45f;//Vocing threshold
    private float unvocingthres = 0.3f;//Vocing threshold
    private float octavec = 0.01f;//Octave cost
    private float Octavejump = 0.35f;//Octave-jump cost
    private float vuvcost = 0.14f;//Voiced/Unvoiced cost

    public f0detector() {
    }

    public float[] sig_f0(float[] sig, int Fs) {
        //This function normalize the speech signal.
        winlen = (float) 0.03;//Default window length 40ms
        winstep = (float) 0.015;//Default window step 30ms;
        fs = Fs;
        inilag = (int) Math.ceil(fs/maxf0);//lower bound lag
        endlag = (int) Math.ceil(fs/minf0);//upper bound lag
        //Get f0 contour from signal
        //Log.e("F0", "Inicia");
        //float[] pitchC = f0_cont(sig);
        float[] pitchC = f0_cont_fast(sig);

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

    public float[] sig_f0(float[] sig, int Fs, float sel_winlen, float sel_winstep) {
        winlen = sel_winlen;
        winstep = sel_winstep;
        fs = Fs;
        inilag = (int) Math.ceil(fs/maxf0);//lower bound lag
        endlag = (int) Math.ceil(fs/minf0);//upper bound lag
        //Get f0 contour from signal
        //Log.e("F0", "Inicia");
        float[] pitchC = f0_cont(sig);
        //Log.e("F0", "Listo");

        //Fix contour.
        pitchC = fixf0(pitchC);

        //Set pitch zero for windows less than the analysis window
        pitchC = zerof0(pitchC);

        //Interpolate missing values
        pitchC = interpf0(pitchC);
        return pitchC;
    }

    //Pitch contour
    public float[] f0_cont(float[] signal) {
        //sigproc SigProc = new sigproc();
        //signal = SG.normalizesig(signal);

        //Find global absolute peak
        float[] temp = Arrays.copyOfRange(signal, 0, signal.length - 1);
        Arrays.sort(temp);
        float GAP = temp[temp.length - 1];
        GAP = Math.abs(GAP);//Global Absolute Peak
        float GAP2 = temp[0];
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
            numberOfframes = (int) (Math.floor(signal.length / numberOfsamples));
            windowshift = numberOfframes;
        } else {
            float temp2 = winstep / winlen;
            numberOfframes = (int) (Math.floor((signal.length / numberOfsamples) / temp2));
        }
        float sig_frame[] = copyOfRange(signal, ini_frame, end_frame);
        //-----------------------------------------------------------------------------------------
        //Compute ACF using FFT
        zpad = 2 * sig_frame.length;
        //Append half a window length of zeroes
        while ((zpad & (zpad - 1)) != 0)//Append zeroes until the number of samples is a power of two
        {
            zpad = zpad + 1;
        }
        fft = new FFT(zpad, fs);
        //Calculate ACF for hanning window. This is used to normalize the ACF (r_sig/r_win)
        float[] winfun = new float[zpad];
        Arrays.fill(winfun, 1);
        winfun = SG.makeWindow(winfun, 1);
        winRx = Arrays.copyOfRange(winfun, (int) Math.ceil(((winfun.length) / 2)), winfun.length);
        winRx = Arrays.copyOfRange(winRx,inilag,endlag);
        //-----------------------------------------------------------------------------------------
        float[] f0c = new float[numberOfframes];
        for (int i = 0; i < numberOfframes; i++) {
            sig_frame = copyOfRange(signal, ini_frame, end_frame);
            //Voiceless candidate
            temp = copyOfRange(sig_frame, 0, sig_frame.length);//it is necessary
            Arrays.sort(temp);
            float LAP = temp[temp.length - 1];//Local Absolute Peak
            LAP = Math.abs(LAP);
            float LAP2 = temp[0];
            LAP2 = Math.abs(LAP2);//in case that the maximum is negative
            if (LAP < LAP2) {
                LAP = LAP2;
            }

            if (LAP < (silencetrehs * GAP)) {
                f0c[i] = 0;
            } else {
                //Estimate pitch for each speech segment
                f0c[i] = f0_boers(sig_frame);
            }
            ini_frame = ini_frame + windowshift;
            end_frame = end_frame + windowshift;
        }
        return f0c;
    }



    //Pitch contour
    public float[] f0_cont_fast(float[] signal) {
        //sigproc SigProc = new sigproc();
        //signal = SG.normalizesig(signal);

        //Find global absolute peak
        float[] temp = Arrays.copyOfRange(signal, 0, signal.length - 1);
        Arrays.sort(temp);
        float GAP = temp[temp.length - 1];
        GAP = Math.abs(GAP);//Global Absolute Peak
        float GAP2 = temp[0];
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
            numberOfframes = (int) (Math.floor(signal.length / numberOfsamples));
            windowshift = numberOfframes;
        } else {
            float temp2 = winstep / winlen;
            numberOfframes = (int) (Math.floor((signal.length / numberOfsamples) / temp2));
        }
        float sig_frame[] = copyOfRange(signal, ini_frame, end_frame);
        //-----------------------------------------------------------------------------------------
        //Compute ACF using FFT
        zpad = 2 * sig_frame.length;
        //Append half a window length of zeroes
        while ((zpad & (zpad - 1)) != 0)//Append zeroes until the number of samples is a power of two
        {
            zpad = zpad + 1;
        }
        fft = new FFT(zpad, fs);
        //Calculate ACF for hanning window. This is used to normalize the ACF (r_sig/r_win)
        float[] winfun = new float[zpad];
        Arrays.fill(winfun, 1);
        winfun = SG.makeWindow(winfun, 1);
        winRx = Arrays.copyOfRange(winfun, (int) Math.ceil(((winfun.length) / 2)), winfun.length);
        winRx = Arrays.copyOfRange(winRx,inilag,endlag);
        //-----------------------------------------------------------------------------------------
        float[] f0c = new float[numberOfframes];
        for (int i = 0; i < numberOfframes; i++) {
            f0c[i]=0;
            if ((i % 2) == 0){
                sig_frame = copyOfRange(signal, ini_frame, end_frame);
                //Voiceless candidate
                temp = copyOfRange(sig_frame, 0, sig_frame.length);//it is necessary
                Arrays.sort(temp);
                float LAP = temp[temp.length - 1];//Local Absolute Peak
                LAP = Math.abs(LAP);
                float LAP2 = temp[0];
                LAP2 = Math.abs(LAP2);//in case that the maximum is negative
                if (LAP < LAP2) {
                    LAP = LAP2;
                }

                if (LAP < (silencetrehs * GAP)) {
                    f0c[i] = 0;
                } else {
                    //Estimate pitch for each speech segment
                    f0c[i] = f0_boers(sig_frame);
                }

            }
            else{
                if (i>2) {
                    if (f0c[i - 3]!=0 &&  f0c[i - 1]!=0){
                        f0c[i - 2] = (f0c[i - 3] + f0c[i - 1]) / 2;
                    }
                    else if (f0c[i - 3]==0 &&  f0c[i - 1]!=0){
                        f0c[i - 2] = f0c[i - 1];
                    }
                    else if (f0c[i - 3]!=0 &&  f0c[i - 1]==0){
                        f0c[i - 2] = f0c[i - 3];
                    }
                    else{
                        f0c[i-2]=0;
                    }

                }
            }

            ini_frame = ini_frame + windowshift;
            end_frame = end_frame + windowshift;
        }
        return f0c;
    }





    //Calculated pitch according to Paul Boersma method(Praat)
    private float f0_boers(float[] sig_frame) {
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
                NRx[i] = (sigRx[i]/maxACF) / winRx[i];
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

    //Autocorrelation (ACF). Convolution
    public void acf(float[] x, float[] ac) {
        Arrays.fill(ac, 0);
        int n = x.length;
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                ac[j] += x[i] * x[(n + i - j) % n];
            }
        }
    }

    //Find first min of ACF
    private int findmin(float[] rx)
    {
        //sigproc SG = new sigproc();
        float[] d = ArrM.diff(rx);
        int ind = 0;
        for (int i=0;i<d.length;i++)
        {
            if (d[i]>0)
            {
                ind=i;
                break;
            }
            else
                ind = 0;
        }
        return ind;
    }

    //Fix contour
    //Unusual variations in pitch in signals.
    //cont: f0cont
    private float[] fixf0(float[] cont) {
        //List maxf0 = SG.find(cont, 500, 2);//Find pitch values greater than 500Hz
        //List minf0 = SG.find(cont, 75, 0);//Find pitch values less than 75Hz
        float[] d = ArrM.diff(cont);
        float[] d_abs = ArrM.absArr(d);//Absolute value of differences
        List df0 = ArrM.find(d_abs, 80, 2);//Find pitch variations greater than 80Hz
        if (df0.size()>0) {
            //Set to zero pitch values with high variations (80Hz)

            for (int i = 0; i < df0.size(); i++) {
                int ind = (int) df0.get(i);
                cont[ind+1] = 0;
            }}
            /*
            //Set to zero pitch values greater than 500Hz
            for (int i = 0; i < maxf0.size(); i++) {
                int ind = (int) maxf0.get(i);
                cont[ind] = 0;
            }
            //Set to zero pitch values less than 75Hz
            for (int i = 0; i < minf0.size(); i++) {
                int ind = (int) minf0.get(i);
                cont[ind] = 0;
            }*/

        return cont;
    }


    //Set f0 to zero in frames with a length less than the analysis window
    public float[] zerof0(float[] cont) {
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
    public float[] interpf0(float[] cont) {
        //sigproc SigProc = new sigproc();
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
                        float[] f0intp = ArrM.interpolate(cont[posIni], cont[posfin], diff);
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

    /***
     * Voiced speech signal
     * @param f0 pitch contour (sig_f0)
     * @param sigN Normalized speech signal (DC=0, Amplitude from -1 to +1)
     * @return List with the voiced segments
     * */
    public List<float[]> voiced(float[] f0,float[] sigN)
    {
        int minseg = (int) Math.ceil(winlen * fs);
        int step = Math.round(winstep*fs);
        List<float[]> VoicedSeg = new ArrayList<float[]>();
        //sigproc SG = new sigproc();
        List pitchON = ArrM.find(f0,0f,5);//Find different than 0
        //List to array
        float[] f0ON = new float[pitchON.size()];
        for(int i=0;i<pitchON.size();i++)
        {
            int ind = (int) pitchON.get(i);
            f0ON[i] =  ind;
        }
        //Initial position of segment
        int iniV = (int) pitchON.get(0)*step+step;
        //Detect segments
        float[] df0 = ArrM.diff(f0ON);
        List sizeV = ArrM.find(df0,1,2);
        for(int i=0;i<sizeV.size();i++)
        {
            //int indx = sizV[i];
            int indx = (int) sizeV.get(i);
            int finV = (int) (pitchON.get(indx))*step+step;
            float[] seg = copyOfRange(sigN,iniV,finV);
            iniV = (int) pitchON.get(indx+1)*step+step;
          //  if (seg.length>=minseg) {
                VoicedSeg.add(seg);
            //}
        }
        //Append last segment
        int temp = pitchON.size();
        int finV = (int) pitchON.get(temp-1)*step+step;
        float[] seg = copyOfRange(sigN,iniV,finV);
        //if (seg.length>=minseg) {
            VoicedSeg.add(seg);
        //}
        return VoicedSeg;
    }
    /***
     * Unvoiced speech signal
     * @param f0 pitch contourn (sig_f0)
     * @param sigN Normalized speech signal (DC=0, Amplitude from -1 to +1)
     * @return List with the voiced segments
     * */
    public List<float[]> unvoiced(float[] f0,float[] sigN)
    {
        int step = Math.round(winstep*fs);
        List<float[]> UnvoicedSeg = new ArrayList<float[]>();
        //sigproc SG = new sigproc();
        List pitchOFF = ArrM.find(f0,0f,4);//Find equals to 0
        //List to array
        float[] f0OFF = new float[pitchOFF.size()];
        for(int i=0;i<pitchOFF.size();i++)
        {
            int ind = (int) pitchOFF.get(i);
            f0OFF[i] =  ind;
        }
        //Initial position of segment
        int iniV = (int) pitchOFF.get(0)*step+step;
        //Detect segments
        float[] df0 = ArrM.diff(f0OFF);
        List sizeV = ArrM.find(df0,1,2);
        //List to array
        int[] sizV = new int[sizeV.size()];
        for(int i=0;i<sizeV.size();i++)
        {
            sizV[i] = (int) sizeV.get(i);
        }
        //Detect unvoiced segments
        for(int i=0;i<sizV.length;i++)
        {
            int indx = sizV[i];
            int finV = (int) (pitchOFF.get(indx))*step+step;
            float[] seg = copyOfRange(sigN,iniV,finV);
            if (seg.length<(Math.round(0.27*fs))) //It is silence(270ms) or unvoiced
            {
                UnvoicedSeg.add(seg);
            }
            iniV = (int) pitchOFF.get(indx + 1) * step + step;
        }
        //Add last segment
        int temp = pitchOFF.size();
        int finV = (int) pitchOFF.get(temp-1)*step+step;
        float[] seg = copyOfRange(sigN,iniV,finV);
        if (seg.length<(Math.round(0.27*fs))) //It is silence(270ms) or unvoiced
        {
            UnvoicedSeg.add(seg);
        }

        return UnvoicedSeg;
    }


}
