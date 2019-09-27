package com.sma2.sma2.SignalRecording;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.sma2.sma2.DataAccess.SignalDA;
import com.sma2.sma2.DataAccess.SignalDataService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CSVFileWriter {
    public Handler mHandler;
    private final String DELIMITER = ";";
    private final String NEW_LINE = "\r\n";
    private final String FILE_ENDING = ".csv";
    public final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";
    private static final String TAG = CSVFileWriter.class.getSimpleName();
    public static String file_path = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";
    private String filename;
    private BufferedWriter mBufferedWriter = null;






    public CSVFileWriter(String exerciseName) throws IOException {
        String currTime = new SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.getDefault()).format(new Date());
        this.filename = exerciseName + currTime + FILE_ENDING;

        File file = openFile(PATH, this.filename);



        try {
            mBufferedWriter = new BufferedWriter(new FileWriter(file));
        } catch (Exception e) {
            Log.e(TAG, "ERROR opening file");
            throw e;
        }

        HandlerThread ht = new HandlerThread("CSVFileWriterThread");
        ht.start();
        mHandler = new Handler(ht.getLooper()) {
            public void handleMessage(Message msg) {
                try {
                    write((String[]) msg.obj);
                } catch (Exception e) {
                    e.printStackTrace();
                    //TODO: handle exception
                }
            }
        };
    }

    public CSVFileWriter(String exerciseName, String path) throws IOException {
        String currTime = new SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.getDefault()).format(new Date());
        this.filename = exerciseName + currTime + FILE_ENDING;

        File file = openFile(path, this.filename);



        try {
            mBufferedWriter = new BufferedWriter(new FileWriter(file));
        } catch (Exception e) {
            Log.e(TAG, "ERROR opening file");
            throw e;
        }

        HandlerThread ht = new HandlerThread("CSVFileWriterThread");
        ht.start();
        mHandler = new Handler(ht.getLooper()) {
            public void handleMessage(Message msg) {
                try {
                    write((String[]) msg.obj);
                } catch (Exception e) {
                    e.printStackTrace();
                    //TODO: handle exception
                }
            }
        };
    }





    public String getFileName() {
        return this.filename;
    }

    private File openFile(String path, String fileName) throws IOException {
        // check if directory exists and if not create it
        File directory = new File(path);
        if (!directory.exists()) {
            Log.i(TAG, "Directory did not exists, creating: " + path);
            directory.mkdirs();
        }

        // Try to create File
        File file = new File(path + fileName);
        if (!file.exists()) {
            try {
                Log.i(TAG, "Try to create new File at: " + path + fileName);
                file.createNewFile();
            } catch (IOException e) {
                throw e;
            }
            Log.i(TAG, "File Successfully created at: " + path + fileName);
        } else {
            Log.i(TAG, "File: " + path + fileName + " already exists!");
        }
        return file;
    }

    public void writeData(String[] str) {
        Message msg = new Message();
        msg.obj = str;
        mHandler.sendMessage(msg);
    }

    public void write(String[] str) throws IOException {
        String line = "";
        for (int i = 0; i < str.length - 1; i++) {
            line += str[i] + DELIMITER;
        }
        line += str[str.length - 1] + NEW_LINE;
        try {
            mBufferedWriter.write(line);
        } catch (Exception e) {
            Log.e(TAG, "ERROR writing file");
            throw e;
        }
    }

    public void close() throws IOException {
        try {
            mBufferedWriter.flush();
            mBufferedWriter.close();
            mBufferedWriter = null;
        } catch (Exception e) {
            Log.e(TAG, "ERROR on completing writer!");
            e.printStackTrace();
            throw e;
        }
    }

    public static String getpath(){
        return file_path;
    }
}