package app.xunxun.homeclock.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by fengdianxun on 15-6-24.
 */
object FocusTimePreferencesDao {
    private val KEY = "FocusTimePreferencesDao"

    operator fun set(context: Context, value: Long) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putLong(KEY, value)
        editor.apply()
    }

    operator fun get(context: Context): Long {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getLong(KEY, 0)
    }

}
