package app.xunxun.homeclock.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by fengdianxun on 15-6-24.
 */
public class IsLauncherPreferencesDao {
    private static String KEY = IsLauncherPreferencesDao.class.getSimpleName();

    public static void set(Context context, boolean islauncher) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY, islauncher);
        editor.apply();
    }

    public static boolean get(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(KEY, false);
    }

}
