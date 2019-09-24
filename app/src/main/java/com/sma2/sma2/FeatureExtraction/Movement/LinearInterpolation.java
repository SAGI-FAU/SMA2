package com.sma2.sma2.FeatureExtraction.Movement;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.util.ArrayList;
import java.util.List;

public class LinearInterpolation {
    static public List<Double> interpolateLinear(List<Double> data, List<Double> time, List<Double> newTime) {
        List<Double> newData = new ArrayList<>();
        LinearInterpolator interpolatorClass = new LinearInterpolator();
        PolynomialSplineFunction interpolator = interpolatorClass.interpolate(
                ArrayUtils.toPrimitive(time.toArray(new Double[0])),
                ArrayUtils.toPrimitive(data.toArray(new Double[0])));

        for (int i = 0; i < newTime.size(); i++) {
            newData.add(interpolator.value(newTime.get(i)));
        }
        return newData;
    }


}
