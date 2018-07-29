package app.xunxun.homeclock.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * 显示秒针.
 * Created by fengdianxun on 15-6-24.
 */
object ShowSecondPreferencesDao {
    private val KEY = "ShowSecondPreferencesDao"

    operator fun set(context: Context, flag: Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(KEY, flag)
        editor.apply()
    }

    operator fun get(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(KEY, true)
    }

}
