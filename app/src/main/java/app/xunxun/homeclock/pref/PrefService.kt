package app.xunxun.homeclock.pref


import android.graphics.Color
import app.xunxun.homeclock.model.Pic

/**
 * pref名称定义
 * Created by fengdianxun on 2017/11/20.
 */
interface PrefService {

    /**
     * 背景色
     */
    @PrefInt(key = "BackgroundColorPreferencesDao", defaultValue = Color.BLACK)
    fun backgroundColor(): Call<Int>

    /**
     * 背景模式
     */
    @PrefInt(key = "BackgroundModePreferencesDao", defaultValue = MODE_COLOR)
    fun backgroundMode(): Call<Int>

    /**
     * 防烧瓶.
     */
    @PrefBoolean(key = "EnableProtectScreenPreferencesDao", defaultValue = false)
    fun enableProtectScreen(): Call<Boolean>

    /**
     * 整点震动.
     */
    @PrefBoolean(key = "EnableSeapkWholeTimePreferencesDao", defaultValue = false)
    fun enableVibrateWholeTime(): Call<Boolean>
    /**
     * 整点语音报时.
     */
    @PrefBoolean(key = "EnableVoiceWholeTimePreferencesDao", defaultValue = false)
    fun enableVoiceWholeTime(): Call<Boolean>

    @PrefBody(key = "test")
    fun test(): Call<Pic>

}


const val MODE_COLOR = 0
const val MODE_ONLINE_IMAGE = 1
const val MODE_LOCAL_IMAGE = 2
