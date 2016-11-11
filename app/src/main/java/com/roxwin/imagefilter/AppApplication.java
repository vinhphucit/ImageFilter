package com.roxwin.imagefilter;

import android.app.Application;
import android.content.Context;

import com.roxwin.imagefilter.utils.Toaster;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
/**
 * Created by PhucTV on 4/18/16.
 */
public class AppApplication extends Application {
    private static final String TAG = AppApplication.class.getName();
    private static Context sContext;

    private static AppApplication sInstance = null;

    public static AppApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sInstance = this;
    }

    public static Context getContext() {
        return sContext;
    }

    public void showToast(String text) {
        Toaster.showToast(this, text);
    }

}
