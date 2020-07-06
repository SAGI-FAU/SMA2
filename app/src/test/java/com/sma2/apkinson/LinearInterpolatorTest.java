package com.sma2.apkinson;

import com.sma2.apkinson.FeatureExtraction.Movement.LinearInterpolation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LinearInterpolatorTest {

    @Test
    public void testSimpleInterpolate() throws Exception {
        List<Double> testTime = new ArrayList<>(Arrays.asList(1., 2., 3., 4.));
        List<Double> testNewTime = new ArrayList<>(Arrays.asList(1.5, 2.5, 3.5));
        List<Double> testData = new ArrayList<>(Arrays.asList(2., 3., 4., 5.));
        List<Double> testNewData = new ArrayList<>(Arrays.asList(2.5, 3.5, 4.5));
        List<Double> testResult = LinearInterpolation.interpolateLinear(testData, testTime, testNewTime);
        assertEquals(testNewData, testResult);
    }

    @Test
    public void testSimpleInterpolateToSamplingRate() throws Exception {
        List<Double> testTime = new ArrayList<>(Arrays.asList(1., 3.));
        List<Double> testData = new ArrayList<>(Arrays.asList(2., 4.));
        List<Double> testNewData = new ArrayList<>(Arrays.asList(2., 2.5, 3., 3.5, 4.));
        List<Double> testResult = LinearInterpolation.interpolateLinearToSamplingRate(testData, testTime, 2.);
        assertEquals(testNewData, testResult);
    }
}
