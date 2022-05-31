package com.otl.tarangplus.BroadcastReciver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private NetworkChangeListener listener;
    private static boolean isNetworkConnected = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getExtras() == null)
            return;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = manager.getActiveNetworkInfo();

        if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
            if (listener != null) {
                isNetworkConnected = true;
                listener.onNetworkConnected();
            }
        } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            if (listener != null) {
                isNetworkConnected = false;
                listener.onNetworkDisconnected();
            }
        }
    }

    public static boolean isNetworkConnected() {
        return isNetworkConnected;
    }

    public interface NetworkChangeListener {
        void onNetworkDisconnected();

        void onNetworkConnected();
    }

    public void setNetworkChangeListener(NetworkChangeListener listener) {
        this.listener = listener;
    }


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
