package app.xunxun.homeclock.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 整点语音报时.
 * Created by fengdianxun on 15-6-24.
 */
public class EnableSeapkWholeTimePreferencesDao {
    private static final String KEY = "EnableSeapkWholeTimePreferencesDao";

    public static void set(Context context, boolean flag) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY, flag);
        editor.apply();
    }

    public static boolean get(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(KEY, false);
    }

}
