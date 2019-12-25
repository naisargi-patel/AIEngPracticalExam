package com.example.aiengpracticalexam.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.text.SimpleDateFormat;

public class Utility {

    public static String getFormatDate(String date) {
        if (date == null || date.length() == 0)
            return "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

        try {
            return output.format(dateFormat.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
                //Connected
                return true;
            } else {
                // not connected to the internet
                return false;
            }
        } catch (Exception exception) {
            Log.e("Network issue", ""+exception.getLocalizedMessage());
            return false;
        }
    }
}
