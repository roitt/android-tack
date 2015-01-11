package com.rohitbhoompally.tack.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;

/**
 * Created by bearcatmobile on 1/8/15.
 */
public class SharedPrefHandler {
    public static final String PREFS_NAME = "TackPrefs";
    public static final String CAMERA_WHICH = "camera_which";
    public static final String FLASH_MODE = "flash_mode";

    private static SharedPreferences.Editor getSharedPreferencesEditor(
            Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit();
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static boolean setCameraWhich(Context context, int value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putInt(CAMERA_WHICH, value);
        return editor.commit();
    }

    public static int getCameraWhich(Context context) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getInt(CAMERA_WHICH, 0);
    }

    public static boolean setFlashMode(Context context, String value) {
        SharedPreferences.Editor editor = getSharedPreferencesEditor(context);
        editor.putString(FLASH_MODE, value);
        return editor.commit();
    }

    public static String getFlashMode(Context context) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getString(FLASH_MODE, Camera.Parameters.FLASH_MODE_OFF);
    }

}
