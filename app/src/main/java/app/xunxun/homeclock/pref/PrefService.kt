package app.xunxun.homeclock.pref


import android.content.pm.ActivityInfo
import android.graphics.Color

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
     * 字体颜色
     */
    @PrefInt(key = "TextColorPreferencesDao", defaultValue = Color.WHITE)
    fun textColor(): Call<Int>

    /**
     * 字体大小
     */
    @PrefInt(key = "TextSizePreferencesDao", defaultValue = 100)
    fun textSize(): Call<Int>

    /**
     * 背景模式
     */
    @PrefInt(key = "BackgroundModePreferencesDao", defaultValue = MODE_COLOR)
    fun backgroundMode(): Call<Int>

    /**
     *屏幕旋转.
     */
    @PrefInt(key = "ScreenOrientationPreferencesDao", defaultValue = ActivityInfo.SCREEN_ORIENTATION_SENSOR)
    fun screenOrientation(): Call<Int>

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

    /**
     * 12小时制.
     */
    @PrefBoolean(key = "Is12TimePreferencesDao", defaultValue = false)
    fun is12Time(): Call<Boolean>

    /**
     * 启动器模式.
     */
    @PrefBoolean(key = "IsLauncherPreferencesDao", defaultValue = false)
    fun isLauncher(): Call<Boolean>

    /**
     * 冒号闪烁.
     */
    @PrefBoolean(key = "IsMaoHaoShanShuoPreferencesDao", defaultValue = false)
    fun isMaoHaoShanShuo(): Call<Boolean>

    /**
     * 显示电量.
     */
    @PrefBoolean(key = "IsShowBatteryPreferencesDao", defaultValue = false)
    fun isShowBattery(): Call<Boolean>

    /**
     * 显示日期.
     */
    @PrefBoolean(key = "IsShowDatePreferencesDao", defaultValue = true)
    fun isShowDate(): Call<Boolean>

    /**
     * 显示农历.
     */
    @PrefBoolean(key = "IsShowLunarPreferencesDao", defaultValue = true)
    fun isShowLunar(): Call<Boolean>

    /**
     * 显示星期.
     */
    @PrefBoolean(key = "IsShowWeekPreferencesDao", defaultValue = true)
    fun isShowWeek(): Call<Boolean>

    /**
     * 保持屏幕常亮.
     */
    @PrefBoolean(key = "KeepScreenOnPreferencesDao", defaultValue = true)
    fun keepScreenOn(): Call<Boolean>

    /**
     * 锁屏显示.
     */
    @PrefBoolean(key = "LockScreenShowOnPreferencesDao", defaultValue = true)
    fun lockScreenShowOn(): Call<Boolean>

    /**
     * 通知栏常驻.
     */
    @PrefBoolean(key = "NotifyStayPreferencesDao", defaultValue = true)
    fun notifyStay(): Call<Boolean>

    /**
     * 显示秒针.
     */
    @PrefBoolean(key = "ShowSecondPreferencesDao", defaultValue = true)
    fun showSecond(): Call<Boolean>

    /**
     * 提醒时间.
     */
    @PrefLong(key = "FocusTimePreferencesDao", defaultValue = 0)
    fun focusTime(): Call<Long>

    /**
     * 本地图片位置.
     */
    @PrefString(key = "LocalImageFilePathPreferencesDao", defaultValue = "")
    fun localImageFilePath(): Call<String>

    /**
     * 提醒文字.
     */
    @PrefString(key = "TextSpaceContentPreferencesDao", defaultValue = "")
    fun textSpaceContent(): Call<String>

    /**
     * 城市天气.
     */
    @PrefString(key = "weatherCityCode", defaultValue = "")
    fun city(): Call<String>

}


const val MODE_COLOR = 0
const val MODE_ONLINE_IMAGE = 1
const val MODE_LOCAL_IMAGE = 2
