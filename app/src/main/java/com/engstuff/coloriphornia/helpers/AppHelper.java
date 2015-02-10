package com.engstuff.coloriphornia.helpers;

import android.content.Context;

public class AppHelper {

    /**
     * Writing to Preferences
     */


    public static void writeToPrefs(Context ctx, String key, String value) {
        ctx
                .getSharedPreferences(ctx.getApplicationInfo().packageName, ctx.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }

    public static void writeToPrefs(Context ctx, String key, boolean value) {
        ctx
                .getSharedPreferences(ctx.getApplicationInfo().packageName, ctx.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    /**
     * Reading from Preferences
     */

    public static String readFromPrefsString(Context ctx, String key) {
        return ctx
                .getSharedPreferences(ctx.getApplicationInfo().packageName, ctx.MODE_PRIVATE)
                .getString(key, "");
    }

    public static boolean readFromPrefsBoolean(Context ctx, String key) {
        return ctx
                .getSharedPreferences(ctx.getApplicationInfo().packageName, ctx.MODE_PRIVATE)
                .getBoolean(key, false);
    }
}
