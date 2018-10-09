package com.sma2.sma2.SpeechFeatures.features;

import com.sma2.sma2.SpeechFeatures.tools.array_manipulation;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

public class phon_feats {
    private array_manipulation ArrM = new array_manipulation();

    public float jitter(float[] f0) {
        //Ensure nonzero values in f0 contour
        List lf0 = ArrM.find(f0,0f,2);
        //Convert list to float array
        f0 = ArrM.listtoarray(lf0);

        //Length of the f0 contour
        int N = f0.length;
        //Find Max
        float[] temp = Arrays.copyOfRange(f0,0, f0.length);
        Arrays.sort(temp);
        float Mp = temp[temp.length - 1];//Maximum pitch
        //Array with variations between elements of array
        float jitt = 0f;
        for (int i = 0; i < N; i=i+3)
        {
            int M = i+3;//Jitter is computed every 3 f0 periods.
            int im = i;//Initial f0 point.
            if (M>N) //This condition is used to avoid index bound errors
            {
                M = N;
                im = M-3;
            }
            float tempd = 0;
            for (int j = im; j < M; j++)
            {
                tempd+= abs(f0[j] - Mp);
            }
            jitt += tempd;
        }
        jitt = (100*jitt)/(N*Mp);
        return jitt;
    }

}