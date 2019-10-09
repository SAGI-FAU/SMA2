package com.sma2.sma2.FeatureExtraction.Movement;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class PeakDetector {

    static List<Pair<Integer, Double>> findPeakAll(List<Double> data) {
        List<Pair<Integer, Double>> peakPairList = new ArrayList<Pair<Integer, Double>>();
        for (int i = 1; i < data.size() - 1; i++) {
            if (data.get(i) - data.get(i - 1) > 0 && data.get(i) - data.get(i + 1) > 0) {
                Pair<Integer, Double> p = new Pair<Integer, Double>(i, data.get(i));
                peakPairList.add(p);
            }
        }
        return peakPairList;
    }

    static List<Integer> findPeakFiltered(List<Double> data, double thres) {
        List<Pair<Integer, Double>> peakPairList = findPeakAll(data);

        List<Integer> peakList = new ArrayList<Integer>();

        float meanPeakAmplitude = 0.0f;

        for (int i = 0; i < peakPairList.size(); i++) {
            meanPeakAmplitude += peakPairList.get(i).second;
        }
        meanPeakAmplitude /= peakPairList.size();

        for (int i = 0; i < peakPairList.size(); i++) {
            if (peakPairList.get(i).second > thres * meanPeakAmplitude) {
                peakList.add(peakPairList.get(i).first);
            }
        }

        return peakList;
    }

}
