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

    // In MILLISECONDS when to try to check for WIFI and upload again
    // one hour 3600000
    private int repeatTimer = 1800000; // Currently every half an hour

    Timer timer = new Timer();

    public UploadService(){}

    public UploadService(String username, String password, File file, Context context) {
        this.username = username;
        this.password = password;
        this.file = file;
        this.context = context;
    }

    /**
     * Method to time the upload service. Every "repeatTimer" it checks if there is a WIFI
     * connection and tries to upload
     *
     * @param
     *
     * @return
     */
    public void doTimedUpload() {

        timer.scheduleAtFixedRate(new TimerTask() {

          @Override
          public void run() {
              if (isOnline(context)) { // if there is a wifi connection
                  Log.d("Online before upload","Check it out");
                  // Upload the file
                  performUpload(username,password,file);
                  // After upload cancel the timer
                  timer.cancel();
                  return;
              } else {
                  Log.d("NoWIFI", "Wifi not established");
              }
          }
      },
        0, repeatTimer);
    }

    /**
     * Method to perform the file upload
     *
     * @param username
     * @param password
     *
     * @return
     */
    public void performUpload(String username, String password, File file) {

        // Create a transmitFile Object
        FileTransmitter transmitFile = new FileTransmitter(username, password);
        Log.d("Transmitter","new  Transmitter created");
        // Upload the file
        transmitFile.uploadFile(file);
        Log.d("Transmitter","File was uploaded");
    }

    /**
     * Method to check if there is a existing WIFI connection
     *
     * @param context
     *
     * @return true if WIFI connection established, false otherwise
     */
    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        //should check null because in airplane mode it will be null and make sure only WIFI
        // connnection counts
        return (netInfo != null && netInfo.isConnected()&& netInfo.getTypeName().equalsIgnoreCase("WIFI"));
    }
}
