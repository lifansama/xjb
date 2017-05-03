package app.xunxun.homeclock.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;

/**
 * Created by fengdianxun on 15-6-24.
 */
public class ScreenOrientationPreferencesDao {
    private static final String KEY = "ScreenOrientationPreferencesDao";

    public static void set(Context context, int value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY, value);
        editor.apply();
    }

    public static int get(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(KEY, ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

}
