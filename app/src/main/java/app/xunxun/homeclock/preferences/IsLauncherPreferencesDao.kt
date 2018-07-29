package app.xunxun.homeclock.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by fengdianxun on 15-6-24.
 */
object IsLauncherPreferencesDao {
    private val KEY = "IsLauncherPreferencesDao"

    operator fun set(context: Context, islauncher: Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(KEY, islauncher)
        editor.apply()
    }

    operator fun get(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(KEY, false)
    }

}
