package app.xunxun.homeclock.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by fengdianxun on 15-6-24.
 */
public class BackgroundModePreferencesDao {
    private static final String KEY = "BackgroundModePreferencesDao";
    public static final int MODE_COLOR = 0;
    public static final int MODE_ONLINE_IMAGE = 1;
    public static final int MODE_LOCAL_IMAGE = 2;

    public static void set(Context context, int flag) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY, flag);
        editor.apply();
    }

    public static int get(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(KEY, 0);
    }

}
