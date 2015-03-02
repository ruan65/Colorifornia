package com.engstuff.coloriphornia.helpers;

import android.util.Log;

public class Logging {

    public static final String TAG = "ml";

    public static void logMemory() {

        Log.i(TAG, "Total memory = " + Runtime.getRuntime().totalMemory() / 1024);
    }

    public static void log(String msg) {
        Log.d(TAG, msg);
    }
}
