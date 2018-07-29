package app.xunxun.homeclock.preferences

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.preference.PreferenceManager

/**
 * Created by fengdianxun on 15-6-24.
 */
object KeepScreenOnPreferencesDao {
    private val KEY = "KeepScreenOnPreferencesDao"

    operator fun set(context: Context, isOn: Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(KEY, isOn)
        editor.apply()
    }

    operator fun get(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(KEY, true)
    }

}
