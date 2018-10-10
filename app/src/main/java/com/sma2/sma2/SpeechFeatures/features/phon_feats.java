package com.sma2.sma2.SpeechFeatures.features;

import com.sma2.sma2.SpeechFeatures.tools.array_manipulation;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

public class phon_feats {
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

}