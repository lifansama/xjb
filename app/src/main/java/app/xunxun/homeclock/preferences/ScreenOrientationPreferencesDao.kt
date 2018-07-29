package app.xunxun.homeclock.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.preference.PreferenceManager

/**
 * Created by fengdianxun on 15-6-24.
 */
object ScreenOrientationPreferencesDao {
    private val KEY = "ScreenOrientationPreferencesDao"

    operator fun set(context: Context, value: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putInt(KEY, value)
        editor.apply()
    }

    operator fun get(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt(KEY, ActivityInfo.SCREEN_ORIENTATION_SENSOR)
    }

}
