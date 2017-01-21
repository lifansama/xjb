package app.xunxun.homeclock.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by fengdianxun on 15-6-24.
 */
public class IsShowDatePreferencesDao {
    private static String KEY = IsShowDatePreferencesDao.class.getSimpleName();

    public static void set(Context context, boolean isShow) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY, isShow);
        editor.apply();
    }

    public static boolean get(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(KEY, true);
    }

}
