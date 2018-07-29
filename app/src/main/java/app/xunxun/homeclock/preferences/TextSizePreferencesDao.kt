package app.xunxun.homeclock.preferences

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.preference.PreferenceManager

/**
 * Created by fengdianxun on 15-6-24.
 */
object TextSizePreferencesDao {
    private val KEY = "TextSizePreferencesDao"

    operator fun set(context: Context, size: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putInt(KEY, size)
        editor.apply()
    }

    operator fun get(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt(KEY, 100)
    }

}
