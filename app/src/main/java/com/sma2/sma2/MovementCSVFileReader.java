package com.sma2.sma2;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovementCSVFileReader {
    private final String TAG = "CSVFileReader";
    private String path = "";
    private final String SEPARATOR = ";";
    private String tmpLine = "";
    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/";
    private int NUM_HEADER_LINES_MOVEMENT_DATA = 6;

    BufferedReader mBufferedReader;

    public MovementCSVFileReader(String filename) throws FileNotFoundException {
        this.path = PATH + filename;
        mBufferedReader = new BufferedReader(new FileReader(this.path));
    }

    public static class SensorDataFrame {
        private double[] mSensorData;
        private long mTimeStamp;

        public SensorDataFrame(long timestamp, double[] sensorData) {
            mSensorData = sensorData;
            mTimeStamp = timestamp;
        }

        public long getTimeStamp() {
            return mTimeStamp;
        }

        public double[] getSensorData() {
            return mSensorData;
        }

        @Override
        public String toString() {
            return Long.toString(mTimeStamp) + "," + Arrays.toString(mSensorData);
        }
    }

    public List<SensorDataFrame> readAccelerationData() throws IOException {
        Log.d(TAG, "Reading AccelerationData");
        List<SensorDataFrame> sensorDataFrameList = new ArrayList<SensorDataFrame>();

        String[] data;
        // skip header lines
        for (int i = 0; i < NUM_HEADER_LINES_MOVEMENT_DATA; i++) {
            mBufferedReader.readLine();
        }

        //reading acc data from CSV file
        while ((tmpLine = mBufferedReader.readLine()) != null) {
            data = tmpLine.split(SEPARATOR);
            double[] accData = new double[3];
            accData[0] = Double.parseDouble(data[1]);
            accData[1] = Double.parseDouble(data[2]);
            accData[2] = Double.parseDouble(data[3]);
            // removing NaN lines
            if(!Double.isNaN(accData[0])) {
                sensorDataFrameList.add(new SensorDataFrame(Long.parseLong(data[0]), accData));
            }
        }

        return sensorDataFrameList;
    }

    // Just for debugging
    public void printAccFrames(List<SensorDataFrame> dataFrameList) {
            for(SensorDataFrame frame: dataFrameList) {
                Log.d(TAG, frame.toString());
            }
    }

}
