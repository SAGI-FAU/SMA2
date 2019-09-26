package com.sma2.sma2.FeatureExtraction.Movement;

import android.util.Log;

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
        Log.d("step", time.toString());
        List<Double> newData = LinearInterpolation.interpolateLinearToSamplingRate(time, data, SAMPLING_FREQUENCY_HZ);

        List<Double> dataFilt = new ArrayList<Double>();
        for(int i = 0; i < newData.size(); i++) {
            dataFilt.add(butterworth.filter(newData.get(i)));
        }
        return PeakDetector.findPeakFiltered(dataFilt, PEAK_FILTER_THRESHOLD);
    }


}
