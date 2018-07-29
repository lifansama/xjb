package app.xunxun.homeclock.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by fengdianxun on 15-6-24.
 */
object LocalImageFilePathPreferencesDao {
    private val KEY = "LocalImageFilePathPreferencesDao"

    operator fun set(context: Context, text: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(KEY, text)
        editor.apply()
    }

    operator fun get(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(KEY, "")
    }

}
