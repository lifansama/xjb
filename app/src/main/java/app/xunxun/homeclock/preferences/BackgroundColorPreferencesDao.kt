package app.xunxun.homeclock.preferences

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.preference.PreferenceManager

/**
 * Created by fengdianxun on 15-6-24.
 */
object BackgroundColorPreferencesDao {
    private val KEY = "BackgroundColorPreferencesDao"

    operator fun set(context: Context, color: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putInt(KEY, color)
        editor.apply()
    }

    operator fun get(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt(KEY, Color.BLACK)
    }

}
