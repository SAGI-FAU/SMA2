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

    private File openFile(String path, String fileName) {
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
        String path = Environment.getExternalStorageDirectory() + "/Test/";
        Log.d(TAG,"Path: " + path);
        String currTime = new SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.getDefault()).format(new Date());

        File file = openFile(path, currTime + ".csv");

        try {
            mBufferedWriter = new BufferedWriter(new FileWriter(file));
        } catch (Exception e) {
            Log.e(TAG,"ERROR opening file");
            e.printStackTrace();
        }

        writeHeader();
    }

    public void writeHeader() {
        try {
            mBufferedWriter.write("Timestamp" + DELIMITER + "aX [m/s^2]" + DELIMITER + "aY [m/s^2]" + DELIMITER + "aZ [m/s^2]" + DELIMITER + "gX [rad/s]" + DELIMITER + "gY [rad/s]" + DELIMITER + "gZ [rad/s]" + DELIMITER + "mX [tesla]" + DELIMITER + "mY [tesla]" + DELIMITER + "mZ [tesla]" + DELIMITER + "r0 [a.u.]" + DELIMITER + "r1 [a.u.]" + DELIMITER + "r2 [a.u.]" + DELIMITER + "r3 [a.u.]" + NEW_LINE);
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
            //mBufferedWriter.flush();
            mBufferedWriter.close();
            //mBufferedWriter = null;
        } catch (Exception e) {
            Log.e(TAG, "Error on completing writer!");
            e.printStackTrace();
        }
    }


}
