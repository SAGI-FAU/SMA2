package com.sma2.sma2;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MovementCSVFileReader {
    //TODO: this is just a dummy class to test reading back sensor data from CSV files. Implementation is not finished yet!
    private final String TAG = "CSVFileReader";
    private String path = "";
    private final String SEPARATOR = ";";
    private String tmpLine = "";
    private int NUM_HEADER_LINES = 6;

    BufferedReader mBufferedReader;

    private List<double []> accDataList = new ArrayList<double []>();
    private List<Long> timeStampList = new ArrayList<Long>();

    public MovementCSVFileReader(String path) {
        //this.path = path;
        this.path = Environment.getExternalStorageDirectory() + "/Apkinson/" + path;
        try {
            mBufferedReader = new BufferedReader(new FileReader(this.path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFile() {
        try {
            String[] data;
            // skip header lines
            for (int i = 0; i < NUM_HEADER_LINES; i++) {
                mBufferedReader.readLine();
            }

            //reading acc data from CSV file
            while ((tmpLine = mBufferedReader.readLine()) != null) {
                data = tmpLine.split(SEPARATOR);
                timeStampList.add(Long.parseLong(data[0]));
                double [] accSample = new double[3];

                accSample[0] = Double.parseDouble(data[1]);
                accSample[1] = Double.parseDouble(data[2]);
                accSample[2] = Double.parseDouble(data[3]);
                accDataList.add(accSample);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
