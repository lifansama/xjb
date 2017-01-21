package app.xunxun.homeclock.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by fengdianxun on 15-6-24.
 */
public class Is12TimePreferencesDao {
    private static String KEY = Is12TimePreferencesDao.class.getSimpleName();

    public static void set(Context context, boolean is12) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY, is12);
        editor.apply();
    }

    public static boolean get(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(KEY, false);
    }

}
