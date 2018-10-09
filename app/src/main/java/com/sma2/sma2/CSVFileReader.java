package com.sma2.sma2;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVFileReader {
    private final String TAG = "CSVFileReader";
    private String path = "";
    private final String SEPARATOR = ";";
    private String tmpLine = "";

    BufferedReader mBufferedReader;

    private List<double []> accDataList = new ArrayList<double []>();
    private List<Long> timeStampList = new ArrayList<Long>();

    public CSVFileReader(String path) {
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
            tmpLine = mBufferedReader.readLine();
            String[] data = tmpLine.split("=");
            int numHeaderLines =  Integer.parseInt(data[1].trim());
            Log.d(TAG, "Number of headerLines = " + numHeaderLines);
            // skip header lines
            for (int i = 0; i < numHeaderLines; i++) {
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
