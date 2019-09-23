package com.sma2.sma2.FeatureExtraction.Speech.features;



import com.sma2.sma2.FeatureExtraction.Speech.tools.TransitionDectector;
import com.sma2.sma2.FeatureExtraction.Speech.tools.array_manipulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

public class PhonFeatures {
    private array_manipulation ArrM = new array_manipulation();

    public void phon_feats(){}

    public float jitter(float[] f0) {
        //Ensure nonzero values in f0 contour
        List lf0 = ArrM.find(f0,0f,2);
        //Convert list to float array
        //List to array
        float[] newF0 = new float[lf0.size()];
        for(int i=0;i<lf0.size();i++)
        {
            newF0[i] = f0[(int)lf0.get(i)];
        }
        f0 = newF0;

        //Length of the f0 contour
        int N = f0.length;
        if (N==0){
            return 25.0f;
        }

        //Find Max
        float[] temp = Arrays.copyOfRange(f0,0, f0.length);
        Arrays.sort(temp);
        float Mp = temp[temp.length - 1];//Maximum pitch
        //Array with variations between elements of array
        float jitt = 0f;
        for (int i = 0; i < N; i=i+3)//Jitter is computed every 3 f0 periods
        {
            jitt+= abs(f0[i] - Mp);
        }
        jitt = (100*jitt)/(N*Mp);
        return jitt;
    }

    public float shimmer(float[] f0, List<float[]> Amp) {
        //Ensure nonzero values in f0 contour
        List lf0 = ArrM.find(f0,0f,2);

        //extract the frames with f0 other than 0
        List<float[]> newAmp = new ArrayList<>();
        int aux = 0;
        for(int i=0;i<lf0.size();i++)
        {
            aux = (int) lf0.get(i);
            newAmp.add(Amp.get(aux));
        }
        Amp = newAmp;

        //Calculate maximum values ​​of each frame
        float[] Ak = new float[Amp.size()];
        for(int i=0;i<Amp.size();i++){
            float[] temp = Amp.get(i);
            Arrays.sort(temp);
            float max = temp[temp.length-1];
            max = Math.abs(max);
            float min = temp[0];
            min = Math.abs(min);
            if (max<min)
                max = min;
            Ak[i] = max;
        }

        //Length of the f0 contour
        int N = Ak.length;
        //Find Max
        float[] temp = Arrays.copyOfRange(Ak,0, Ak.length);
        Arrays.sort(temp);
        float Ma = temp[temp.length - 1];//Maximum amplitude
        //Array with variations between elements of array
        float shimm = 0f;
        for (int i = 0; i < N; i=i+3)//Shimmer is computed every 3 f0 periods
        {
            shimm+= abs(Ak[i] - Ma);
        }
        shimm = (100*shimm)/(N*Ma);
        return shimm;
    }

    public float fluency_cal(float[] f0) {
        TransitionDectector transitionDectector = new TransitionDectector();
        ArrayList<ArrayList<Integer>> data = transitionDectector.detect(f0);
        ArrayList<Integer> index_array_onset = data.get(0);
        ArrayList<Integer> index_array_offset = data.get(1);

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


    public float calculateSD(List<Float> numArray)
    {
        float sum = 0, standardDeviation = 0;
        int length = numArray.size();

        for(float num : numArray) {
            sum += num;
        }

        float mean = sum/length;

        for(float num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return (float) Math.sqrt(standardDeviation/length);
    }

}