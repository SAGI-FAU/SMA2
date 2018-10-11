package com.sma2.sma2;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
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

    List<SensorDataFrame> accDataFrameList = new ArrayList<SensorDataFrame>();
    List<SensorDataFrame> gyroDataFrameList = new ArrayList<SensorDataFrame>();
    List<SensorDataFrame> magDataFrameList = new ArrayList<SensorDataFrame>();
    List<SensorDataFrame> rotationDataFrameList = new ArrayList<SensorDataFrame>();

    public MovementCSVFileReader(String filename) throws IOException {
        this.path = PATH + filename;
        mBufferedReader = new BufferedReader(new FileReader(this.path));
        readInMotionData();
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

    private void readInMotionData() throws IOException {
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
            if (!Double.isNaN(accData[0])) {
                accDataFrameList.add(new SensorDataFrame(Long.parseLong(data[0]), accData));
            }

            double[] gyroData = new double[3];
            gyroData[0] = Double.parseDouble(data[4]);
            gyroData[1] = Double.parseDouble(data[5]);
            gyroData[2] = Double.parseDouble(data[6]);
            // removing NaN lines
            if (!Double.isNaN(gyroData[0])) {
                gyroDataFrameList.add(new SensorDataFrame(Long.parseLong(data[0]), gyroData));
            }

            double[] magData = new double[3];
            magData[0] = Double.parseDouble(data[7]);
            magData[1] = Double.parseDouble(data[8]);
            magData[2] = Double.parseDouble(data[9]);
            // removing NaN lines
            if (!Double.isNaN(magData[0])) {
                magDataFrameList.add(new SensorDataFrame(Long.parseLong(data[0]), magData));
            }

            double[] rotationData = new double[4];
            rotationData[0] = Double.parseDouble(data[10]);
            rotationData[1] = Double.parseDouble(data[11]);
            rotationData[2] = Double.parseDouble(data[12]);
            rotationData[3] = Double.parseDouble(data[13]);
            // removing NaN lines
            if (!Double.isNaN(rotationData[0])) {
                rotationDataFrameList.add(new SensorDataFrame(Long.parseLong(data[0]), rotationData));
            }
        }
    }

    public List<SensorDataFrame> getAccDataFrameList() {
        return accDataFrameList;
    }

    public List<SensorDataFrame> getGyroDataFrameList() {
        return gyroDataFrameList;
    }

    public List<SensorDataFrame> getMagDataFrameList() {
        return magDataFrameList;
    }

    public List<SensorDataFrame> getRotationDataFrameList() {
        return rotationDataFrameList;
    }

    // Just for debugging
    public void printSensorDataFrames(List<SensorDataFrame> dataFrameList) {
        for (SensorDataFrame frame : dataFrameList) {
            Log.d(TAG, frame.toString());
        }
    }

}
