package app.xunxun.homeclock.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by fengdianxun on 15-6-24.
 */
object BackgroundModePreferencesDao {
    private val KEY = "BackgroundModePreferencesDao"
    val MODE_COLOR = 0
    val MODE_ONLINE_IMAGE = 1
    val MODE_LOCAL_IMAGE = 2

    operator fun set(context: Context, flag: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putInt(KEY, flag)
        editor.apply()
    }

    operator fun get(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt(KEY, 0)
    }

}
