package com.engstuff.coloriphornia.helpers;

import android.content.Context;

import java.util.Map;

public abstract class PrefsHelper {

    /**
     * Writing to Preferences
     */

    public static void writeToPrefsDefault(Context ctx, String key, String value) {
        ctx
                .getSharedPreferences(ctx.getApplicationInfo().packageName, ctx.MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }

    public static void writeToPrefsDefault(Context ctx, String key, boolean value) {
        ctx
                .getSharedPreferences(ctx.getApplicationInfo().packageName, ctx.MODE_PRIVATE)
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    public static void writeToPrefsDefault(Context ctx, String key, int value) {
        ctx
                .getSharedPreferences(ctx.getApplicationInfo().packageName, ctx.MODE_PRIVATE)
                .edit()
                .putInt(key, value)
                .apply();
    }

    public static void writeToPrefs(Context ctx, String name, String key, int value) {
        ctx
                .getSharedPreferences(name, ctx.MODE_PRIVATE)
                .edit()
                .putInt(key, value)
                .apply();
    }

    public static void writeToPrefs(Context ctx, String name, String key, String value) {
        ctx
                .getSharedPreferences(name, ctx.MODE_PRIVATE)
                .edit()
                .putString(key, value)
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

    public static String readFromPrefsString(Context ctx, String name, String key) {
        return ctx
                    .getSharedPreferences(name, ctx.MODE_PRIVATE)
                    .getString(key, "");
    }

    public static boolean readFromPrefsBoolean(Context ctx, String key) {
        return ctx
                    .getSharedPreferences(ctx.getApplicationInfo().packageName, ctx.MODE_PRIVATE)
                    .getBoolean(key, false);
    }

    public static int readFromPrefsInt(Context ctx, String key) {
        return ctx
                    .getSharedPreferences(ctx.getApplicationInfo().packageName, ctx.MODE_PRIVATE)
                    .getInt(key, 0);
    }

    public static int readFromPrefsInt(Context ctx, String name, String key) {
        return ctx
                    .getSharedPreferences(name, ctx.MODE_PRIVATE)
                    .getInt(key, 0);
    }

    public static Map<String, ?> readFromPrefsAll(Context ctx) {
        return ctx
                    .getSharedPreferences(ctx.getApplicationInfo().packageName, ctx.MODE_PRIVATE)
                    .getAll();
    }

    public static Map<String, ?> readFromPrefsAll(Context ctx, String name) {
        return ctx
                    .getSharedPreferences(name, ctx.MODE_PRIVATE)
                    .getAll();
    }

    public static String[] readFromPrefsAllToArray(Context ctx, String name) {

        Map<String, ?> map = ctx.getSharedPreferences(name, ctx.MODE_PRIVATE)
                .getAll();

        return map.keySet().toArray(new String[map.size()]);
    }

    /**
     * Erasing Preferences
     */

    public static void erasePrefs(Context ctx, String name, String key) {

        ctx
                .getSharedPreferences(name, ctx.MODE_PRIVATE)
                .edit()
                .remove(key)
                .apply();
    }

    public static void eraseAllPrefs(Context ctx, String name) {

        ctx
                .getSharedPreferences(name, ctx.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }

}





























