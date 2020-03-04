package com.patelheggere.farmconnect.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.patelheggere.farmconnect.FarmConnectApplication;
import com.patelheggere.farmconnect.interfaces.OnNetworkChangeListener;

public class NetworkUtil {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    static OnNetworkChangeListener onNetworkChangeListener;

    public static  void setOnNetworkChangeListener(OnNetworkChangeListener onNetworkChangeListener) {
        NetworkUtil.onNetworkChangeListener = onNetworkChangeListener;
    }

    public static int getConnectivityStatus(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                    return TYPE_WIFI;
                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                    return TYPE_MOBILE;
            }
        }catch (Exception e){}
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        String status = "";
        try {
            if (context == null) {
                context = FarmConnectApplication.getInstance().getApplicationContext();
            }
            int conn = NetworkUtil.getConnectivityStatus(context);
            if (conn == NetworkUtil.TYPE_WIFI) {
                status = "Connected to Internet with WIFI";
            } else if (conn == NetworkUtil.TYPE_MOBILE) {
                status = "Connected to Internet with Mobile Data";
            } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
                status = "Internet connection required";
            }
            if (null != onNetworkChangeListener) {
                onNetworkChangeListener.onChange(status);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }

}
