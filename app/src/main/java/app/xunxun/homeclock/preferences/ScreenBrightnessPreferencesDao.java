package app.xunxun.homeclock.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import static android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;

/**
 * Created by fengdianxun on 15-6-24.
 */
public class ScreenBrightnessPreferencesDao {
    private static final String KEY_SYSTEM_SCREEN_BRIGHTNESS_MODE = "systemScreenBrightnessMode";
    private static final String KEY_SYSTEM_SCREEN_BRIGHTNESS_VALUE = "systemScreenBrightnessValue";


    public static void setSysMode(Context context, int value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_SYSTEM_SCREEN_BRIGHTNESS_MODE, value);
        editor.apply();
    }
    public static void setSysValue(Context context, int value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_SYSTEM_SCREEN_BRIGHTNESS_VALUE, value);
        editor.apply();
    }

    public static int getSysMode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(KEY_SYSTEM_SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    public static int getSysValue(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(KEY_SYSTEM_SCREEN_BRIGHTNESS_VALUE, 0);
    }


}
