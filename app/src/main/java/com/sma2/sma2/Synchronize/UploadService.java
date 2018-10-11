package com.sma2.sma2.Synchronize;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class UploadService{

    Context context;

    private String username;
    private String password;
    private File file;

    Timer timer = new Timer();

    public UploadService(){}

    public UploadService(String username, String password, File file, Context context) {
        this.username = username;
        this.password = password;
        this.file = file;
        this.context = context;
    }

    public void doTimedUpload() {

        timer.scheduleAtFixedRate(new TimerTask() {

          @Override
          public void run() {

              if (isOnline(context)) { // if there is a wifi connection

                  Log.e("Online before upload","Check it out");
                  // Upload the file
                  //performUpload(username, password, file);
                  FileTransmitter transmitFile = new FileTransmitter(username, password);
                  Log.e("Transmitter","new  Transmitter created");
                  Log.e("File check",file.getAbsolutePath());
                  transmitFile.uploadFile(file);
                  Log.e("Transmitter","File was uploaded");

                  // After upload cancel the timer
                  timer.cancel();
              }

              Log.e("NoWIFI","Wifi not established");
          }

      },
        0, 1000); //try again in one hour3600000
    }


    public void performUpload(String username, String password, File file) {

        FileTransmitter transmitFile = new FileTransmitter(username, password);
        Log.e("Transmitter","new  Transmitter created");
        transmitFile.uploadFile(file);
        Log.e("Transmitter","File was uploaded");
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        Log.e("Online check",netInfo.getTypeName());
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected()); // MAKE SURE THAT ONLY WIFI IS POSSIBLE
    }

    /*
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
        }
        return haveConnectedWifi;
    }
    */
}
