package com.sma2.apkinson.SendData;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;


public class ConectionWifi {


        private Context invocationcontext;

        public ConectionWifi(Context context){
            this.invocationcontext = context;

        }




    public boolean checkConnection(ConectionWifi cW) {
        WifiManager wifiMgr = (WifiManager) cW.invocationcontext.getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();


            String supState = wifiInfo.getSupplicantState().name();
            if( supState.equals("COMPLETED")){
                return true; // Not connected to an access point
            }
            else {
                return false; // Connected to an access point

            }
        }
        else {
            return false; // Wi-Fi adapter is OFF
        }
    }

}



