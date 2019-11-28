package com.sma2.sma2.SendData;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public class ConectionWifi {


        private Context invocationcontext;

        public ConectionWifi(Context context){
            this.invocationcontext = context;

        }

    public boolean checkConnection(ConectionWifi cW) {

        ConnectivityManager connectivityManager = (ConnectivityManager) cW.invocationcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            String name = networkInfo.getTypeName();
            if (name.equals("mobile")) {

                return false;
            } else {
                return true;
            }

        } else {
            Log.d("Internet", "no ok");
            return false;
        }
    }

}
