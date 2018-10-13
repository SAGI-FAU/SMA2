package com.sma2.sma2.FeatureExtraction.Tapping.SpeechFeatures;

import com.sma2.sma2.FeatureExtraction.Tapping.tools.array_manipulation;
import com.sma2.sma2.FeatureExtraction.Tapping.tools.sigproc;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

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
            powered_array = operations.absArr(segments.get(i)); // Change to operations.powerArray if it is necessary
            window_energy = operations.sumArray(powered_array)/powered_array.length;
            energy_contour[i] = (float) Math.log(window_energy);
        }
        return energy_contour;
    }

    public float perturbationEnergy(float[] energy_contour) {
        //Length of the energy contour
        int N = energy_contour.length;
        //Find Max
        float[] temp = Arrays.copyOfRange(energy_contour,0, energy_contour.length);
        Arrays.sort(temp);
        float Mp = temp[temp.length - 1];//Maximum pitch
        //Array with variations between elements of array
        float energy_perturbation = 0f;
        for (int i = 0; i < N; i=i+3)//Perturbation is computed every 3 f0 periods
        {
            energy_perturbation+= abs(energy_contour[i] - Mp);
        }
        energy_perturbation = (100*energy_perturbation)/(N*Math.abs(Mp));
        return energy_perturbation;
    }
}
