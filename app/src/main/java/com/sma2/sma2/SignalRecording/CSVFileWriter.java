package com.sma2.sma2.SignalRecording;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CSVFileWriter {
    private String TAG = "CSVFileWriter";
    private final String DELIMITER = ";";
    private final String NEW_LINE = "\r\n";

    private BufferedWriter mBufferedWriter = null;
    private String mHeaderString = "";

    private final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/";

    private File openFile(String path, String fileName) {

        // check if directory exists and if not create it
        File directory = new File(path);
        if (! directory.exists()) {
            Log.d(TAG, "Directory did not exists, creating: " + path);
            directory.mkdir();
        }

        // Try to create File
        File file = new File(path + fileName);
        if (!file.exists()) {
            try {
                Log.d(TAG,"Try to create new File at: " + path + fileName);
                file.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG,"File Successfully created at: " + path + fileName);
        } else {
            Log.d(TAG, "File: " + path + fileName + " already exists!");
        }
        return file;
    }

    public CSVFileWriter() {
        String path = PATH;
        Log.d(TAG,"Path: " + path);
        String currTime = new SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.getDefault()).format(new Date());

        File file = openFile(path, currTime + ".csv");

        try {
            mBufferedWriter = new BufferedWriter(new FileWriter(file));
        } catch (Exception e) {
            Log.e(TAG,"ERROR opening file");
            e.printStackTrace();
        }
    }
    public void addHeaderString(String str) {
        mHeaderString += str;
    }

    public void writeHeader() {
        try {
            mHeaderString += "Timestamp [ns]" + DELIMITER + "aX [m/s^2]" + DELIMITER + "aY [m/s^2]" + DELIMITER + "aZ [m/s^2]" + DELIMITER + "gX [rad/s]" + DELIMITER + "gY [rad/s]" + DELIMITER + "gZ [rad/s]" + DELIMITER + "mX [uT]" + DELIMITER + "mY [uT]" + DELIMITER + "mZ [uT]" + DELIMITER + "r0 [a.u.]" + DELIMITER + "r1 [a.u.]" + DELIMITER + "r2 [a.u.]" + DELIMITER + "r3 [a.u.]" + NEW_LINE;
            mBufferedWriter.write(mHeaderString);
        } catch (Exception e) {
            Log.e(TAG,"ERROR writing file");
            e.printStackTrace();
        }
    }

    public void writeDataFrame(MovementRecorder.CombinedSensorDataFrame df) {
        try {
            mBufferedWriter.write(df.toCSVString());
        } catch (Exception e) {
            Log.e(TAG,"ERROR writing file");
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            mBufferedWriter.flush();
            mBufferedWriter.close();
            mBufferedWriter = null;
        } catch (Exception e) {
            Log.e(TAG, "Error on completing writer!");
            e.printStackTrace();
        }
    }


}
