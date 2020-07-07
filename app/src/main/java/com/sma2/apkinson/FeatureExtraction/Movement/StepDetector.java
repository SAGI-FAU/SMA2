package com.sma2.apkinson.FeatureExtraction.Movement;

import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.util.ArrayList;
import java.util.List;

import uk.me.berndporr.iirj.Butterworth;


public class StepDetector {

    private  final double PEAK_FILTER_THRESHOLD = 0.6f;
    private final double SAMPLING_FREQUENCY_HZ = 100.0f;
    private double filterCutoffHz = 4.0f;
    private int filterOder = 12;
    private Butterworth butterworth = null;
    public int getFilterOder() {
        return filterOder;
    }

    public void setFilterOder(int filterOder) {
        this.filterOder = filterOder;
    }

    public double getFilterCutoffHz() {
        return filterCutoffHz;
    }

    public void setFilterCutoffHz(double filterCutoffHz) {
        this.filterCutoffHz = filterCutoffHz;
    }

    public StepDetector() {
        butterworth = new Butterworth();
        butterworth.lowPass(this.filterOder,this.SAMPLING_FREQUENCY_HZ,this.filterCutoffHz);
    }

    public List<Integer> detect(List<Double> time, List<Double> data) {
        PolynomialSplineFunction interpolator = LinearInterpolation.getInterpolator(data, time);
        List<Double> dataFilt = new ArrayList<>();

        double end = time.get(0);
        while (end <= time.get(time.size() - 1)) {
            dataFilt.add(butterworth.filter(interpolator.value(end)));
            end += (1 / SAMPLING_FREQUENCY_HZ) * 1e9;
        }

        return PeakDetector.findPeakFiltered(dataFilt, PEAK_FILTER_THRESHOLD);
    }


}
