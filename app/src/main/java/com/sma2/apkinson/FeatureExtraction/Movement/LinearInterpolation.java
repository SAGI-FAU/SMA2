package com.sma2.apkinson.FeatureExtraction.Movement;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.util.ArrayList;
import java.util.List;

public class LinearInterpolation {

    static public PolynomialSplineFunction getInterpolator(List<Double> data, List<Double> oldTime) {
        LinearInterpolator interpolatorClass = new LinearInterpolator();
        return interpolatorClass.interpolate(
                ArrayUtils.toPrimitive(oldTime.toArray(new Double[0])),
                ArrayUtils.toPrimitive(data.toArray(new Double[0])));
    }

    static public List<Double> interpolateLinear(List<Double> data, List<Double> oldTime, List<Double> newTime) {
        List<Double> newData = new ArrayList<>();

        PolynomialSplineFunction interpolator = getInterpolator(data, oldTime);
        for (int i = 0; i < newTime.size(); i++) {
            newData.add(interpolator.value(newTime.get(i)));
        }
        return newData;
    }
    
//    Old Time and samplingrate need to have the same unit (e.g. s and 1/s or ns and 1/ns
    static public List<Double> interpolateLinearToSamplingRate(List<Double> data, List<Double> oldTime, double newSamplingRateHz) {
        PolynomialSplineFunction interpolator = getInterpolator(data, oldTime);
        List<Double> newData = new ArrayList<>();

        double end = oldTime.get(0);
        while (end <= oldTime.get(oldTime.size() - 1)) {
            newData.add(interpolator.value(end));
            end += 1 / newSamplingRateHz;
        }
        return newData;
    }


}
