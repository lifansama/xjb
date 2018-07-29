package app.xunxun.homeclock.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log

/**
 * Created by fengdianxun on 15-6-24.
 */
object TextSpaceContentPreferencesDao {
    private val KEY = "TextSpaceContentPreferencesDao"

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
