package com.roxwin.imagefilter.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by PhucTV on 6/23/15.
 */
public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getName();

    public static void whenNetworkConnected(Context context, Runnable runnable) {
        if (isConnected(context)) {
            runnable.run();
        }
        onNetworkConnected(context, runnable);
    }

    public static boolean isConnected(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    public static BroadcastReceiver onNetworkConnected(Context context, final Runnable cb) {
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context1, Intent intent) {
                Log.i(NetworkUtils.TAG, "Network connectivity changed.");
                if (NetworkUtils.isConnected(context1)) {
                    Log.i(NetworkUtils.TAG, "Network just connected");
                    cb.run();
                }
            }
        };
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(broadcastReceiver, intentfilter);
        return broadcastReceiver;
    }
}


