package com.sma2.sma2.SpeechFeatures.tools;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.List;

public class Energy {
    public Energy() {
    }

    public float[] energyContour(float[] signal, int fs){
        sigproc tools = new sigproc();
        array_manipulation operations = new array_manipulation();
        float[] powered_array;
        float window_energy;
        List<float[]> segments = tools.sigframe(signal, fs,0.04f, 0.02f);
        float[] energy_contour = new float[segments.size()];
        for(int i = 0; i < segments.size(); i++){
            powered_array = operations.powerArray(segments.get(i));
            window_energy = operations.sumArray(powered_array)/powered_array.length;
            energy_contour[i] = window_energy;
        }
        return energy_contour;
    }
}
