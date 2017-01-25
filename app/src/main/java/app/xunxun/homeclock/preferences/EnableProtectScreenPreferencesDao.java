package app.xunxun.homeclock.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 烧屏保护.
 * Created by fengdianxun on 15-6-24.
 */
public class EnableProtectScreenPreferencesDao {
    private static String KEY = EnableProtectScreenPreferencesDao.class.getSimpleName();

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
