package com.engstuff.coloriphornia.helpers;

import android.util.Log;

import java.util.List;

public class Logging {

    public static final String TAG = "ml";

    public static void logMemory() {

        Log.i(TAG, "Total memory = " + Runtime.getRuntime().totalMemory() / 1024);
    }

    public static void log(String msg) {
        Log.d(TAG, msg);
    }

    public static void logArrayOrList(List list) {

        for (Object s : list) {
            log(s.toString());
        }
    }

    public static void logArrayOrList(String[] list) {

        for (String s : list) {
            log(s);
        }
    }
}