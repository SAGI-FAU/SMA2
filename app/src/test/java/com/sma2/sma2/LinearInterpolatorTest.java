package com.sma2.sma2;

import com.sma2.sma2.FeatureExtraction.Movement.LinearInterpolation;

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
}