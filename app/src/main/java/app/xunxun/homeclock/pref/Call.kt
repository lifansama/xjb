package app.xunxun.homeclock.pref

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson
import java.lang.reflect.Type

/**
 * Created by fengdianxun on 2017/11/20.
 */

class Call<T>(private val context: Context, private val key: String, private val defaultValue: T?) {


    fun set(value: T) {

        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        when (value) {
            is String -> editor.putString(key, value as String)
            is Int -> editor.putInt(key, value as Int)
            is Boolean -> editor.putBoolean(key, value as Boolean)
            is Long -> editor.putLong(key, value as Long)
            else -> editor.putString(key, Gson().toJson(value))

        }
        editor.apply()
    }


    operator fun get(myClass: Class<T>): T? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val value = prefs.getString(key, null)
        return if (value == null) {
            null
        } else {
            Gson().fromJson(value, myClass)
        }
    }

    operator fun get(type: Type): T? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val value = prefs.getString(key, null)
        return if (value == null) {
            null
        } else {
            Gson().fromJson<T>(value, type)
        }
    }


    fun get(): T {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        var value = prefs.all[key]
        Log.v("pref", "key:$key value:$value")

        return (value ?: defaultValue) as T
    }


    fun clear() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.remove(key).apply()
    }
}
