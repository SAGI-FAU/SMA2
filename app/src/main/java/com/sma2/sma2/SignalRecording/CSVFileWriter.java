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

    public void writeLine(String str) {
        try {
            mBufferedWriter.write(str);
        } catch (Exception e) {
            Log.e(TAG,"ERROR writing file");
            e.printStackTrace();
        }
    }

    public void writeData(String[] str) {
        String line = "";
        for(int i = 0; i < str.length - 1; i++) {
            line += str[i] + DELIMITER;
        }
        line += str[str.length-1] + NEW_LINE;
        try {
            mBufferedWriter.write(line);
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
