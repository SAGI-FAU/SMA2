package com.sma2.sma2.SignalRecording;

import android.content.Context;
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
    private static String exercise;
    private OutputStreamWriter fout;




    public TappingRecorder(){

        MOVEMENT_FOLDER = new File(Environment.getExternalStorageDirectory() + File.separator + "APP_NAME" + File.separator + "MOVEMENT");
        if(!MOVEMENT_FOLDER.exists()){
            MOVEMENT_FOLDER.mkdirs();
        }
    }



    public String prepare(String exercise){
        String date = getCurrentDateAsString();
        FILE_CSV  = new File(MOVEMENT_FOLDER.getAbsolutePath() + File.separator + date + "_" + exercise + ".csv");
        try {

            FILE_CSV.createNewFile();
        } catch (IOException e) {
            Log.e("TappingRecorder", e.toString());
            return "ERROR: Could not create file: " + FILE_CSV.getAbsolutePath();
        }
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(FILE_CSV);
        } catch (FileNotFoundException e) {
            Log.e("TappingRecorder", e.toString());
            return "ERROR: Could not create OutputStream";
        }

        return FILE_CSV.getAbsolutePath();
    }







    public void Record(String exercise,String data_sensors){

        String path =prepare(exercise);
        try {



            FileWriter fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data_sensors);
            bw.close();

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
