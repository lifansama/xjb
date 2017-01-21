package app.xunxun.homeclock.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

/**
 * Created by fengdianxun on 15-6-24.
 */
public class TextColorPreferencesDao {
    private static String KEY = TextColorPreferencesDao.class.getSimpleName();

    public static void set(Context context, int color) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY, color);
        editor.apply();
    }

    public static int get(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(KEY, Color.WHITE);
    }

}
