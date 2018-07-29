package app.xunxun.homeclock.preferences

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.preference.PreferenceManager

import android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC

/**
 * Created by fengdianxun on 15-6-24.
 */
object ScreenBrightnessPreferencesDao {
    private val KEY_SYSTEM_SCREEN_BRIGHTNESS_MODE = "systemScreenBrightnessMode"
    private val KEY_SYSTEM_SCREEN_BRIGHTNESS_VALUE = "systemScreenBrightnessValue"


    fun setSysMode(context: Context, value: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putInt(KEY_SYSTEM_SCREEN_BRIGHTNESS_MODE, value)
        editor.apply()
    }

    fun setSysValue(context: Context, value: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putInt(KEY_SYSTEM_SCREEN_BRIGHTNESS_VALUE, value)
        editor.apply()
    }

    fun getSysMode(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt(KEY_SYSTEM_SCREEN_BRIGHTNESS_MODE, SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
    }

    fun getSysValue(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt(KEY_SYSTEM_SCREEN_BRIGHTNESS_VALUE, 0)
    }


}
