package com.sma2.sma2.SignalRecording;

import android.content.Context;
import android.icu.text.AlphabeticIndex;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by paulaperez on 8/10/18.
 */

public class TappingRecorder {
    private static Context CONTEXT;
    private static Handler HANDLER;
    private static DataOutputStream DATA_OUTPUT_STREAM;
    private static File MOVEMENT_FOLDER;
    private static File FILE_CSV;
    private static File FILE_CSV_error;

    private static String exercise;
    private OutputStreamWriter fout;
    private BufferedWriter bw;
    private BufferedWriter ew;

    private static TappingRecorder recorder_instance=null;


    private TappingRecorder(Context context) {
        CONTEXT=context;
        MOVEMENT_FOLDER = new File(Environment.getExternalStorageDirectory() + File.separator + "APP_NAME" + File.separator + "MOVEMENT");
        if (!MOVEMENT_FOLDER.exists()) {
            MOVEMENT_FOLDER.mkdirs();
        }
    }

    public static TappingRecorder getInstance(Context context) {
        if (recorder_instance == null){
            recorder_instance = new TappingRecorder(context);
        }
        return recorder_instance;
    }


    public String prepare(String exercise) {
        String date = getCurrentDateAsString();
        FILE_CSV = new File(MOVEMENT_FOLDER.getAbsolutePath() + File.separator + date + "_" + exercise + ".csv");
        FILE_CSV_error = new File(MOVEMENT_FOLDER.getAbsolutePath() + File.separator + date + "_" + "Error"+exercise + ".csv");

        try {

            FILE_CSV.createNewFile();
            FILE_CSV_error.createNewFile();

        } catch (IOException e) {
            Log.e("TappingRecorder", e.toString());
            return "ERROR: Could not create file: " + FILE_CSV.getAbsolutePath();

        }


        try {

            FileWriter fw = new FileWriter(FILE_CSV);
            bw = new BufferedWriter(fw);
            FileWriter f_ew = new FileWriter(FILE_CSV_error);
            ew = new BufferedWriter(f_ew);

            //return bw;

        } catch (IOException e) {

            e.printStackTrace();
        }

        return FILE_CSV.getAbsolutePath();

    }

    public void TapWriter(String data_sensors){

        try {

            bw.write(data_sensors);
            //return bw;

        } catch (IOException e) {

            e.printStackTrace();
        }



    }


    public void ErrorWriter(String data_sensors){

        try {

            ew.write(data_sensors);
            //return bw;

        } catch (IOException e) {

            e.printStackTrace();
        }



    }


    public void CloseTappingDocument(){
        try {

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void CloseErrorDocument(){
        try {

            ew.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }








    private String getCurrentDateAsString() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
        return dateFormat.format(date);
    }
}

// TODO: Fix this part to save each tapping vector