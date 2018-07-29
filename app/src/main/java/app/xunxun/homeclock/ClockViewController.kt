package app.xunxun.homeclock

import android.Manifest
import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.Vibrator
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import android.text.style.TypefaceSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.GestureDetector
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

//import com.crashlytics.android.Crashlytics;
import com.pgyersdk.crash.PgyCrashManager
import com.pgyersdk.feedback.PgyFeedbackShakeManager
import com.squareup.picasso.Picasso

import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Random
import java.util.Timer
import java.util.TimerTask

import app.xunxun.homeclock.activity.LauncherActivity
import app.xunxun.homeclock.activity.MainActivity
import app.xunxun.homeclock.activity.SettingsActivity
import app.xunxun.homeclock.api.Api
import app.xunxun.homeclock.helper.SoundPoolHelper
import app.xunxun.homeclock.helper.UpdateHelper
import app.xunxun.homeclock.model.Pic
import app.xunxun.homeclock.preferences.BackgroundColorPreferencesDao
import app.xunxun.homeclock.preferences.BackgroundModePreferencesDao
import app.xunxun.homeclock.preferences.EnableProtectScreenPreferencesDao
import app.xunxun.homeclock.preferences.EnableVibrateWholeTimePreferencesDao
import app.xunxun.homeclock.preferences.EnableShakeFeedbackPreferencesDao
import app.xunxun.homeclock.preferences.EnableVoiceWholeTimePreferencesDao
import app.xunxun.homeclock.preferences.FocusTimePreferencesDao
import app.xunxun.homeclock.preferences.Is12TimePreferencesDao
import app.xunxun.homeclock.preferences.IsMaoHaoShanShuoPreferencesDao
import app.xunxun.homeclock.preferences.IsShowBatteryPreferencesDao
import app.xunxun.homeclock.preferences.IsShowDatePreferencesDao
import app.xunxun.homeclock.preferences.IsShowLunarPreferencesDao
import app.xunxun.homeclock.preferences.IsShowWeekPreferencesDao
import app.xunxun.homeclock.preferences.LocalImageFilePathPreferencesDao
import app.xunxun.homeclock.preferences.LockScreenShowOnPreferencesDao
import app.xunxun.homeclock.preferences.ScreenBrightnessPreferencesDao
import app.xunxun.homeclock.preferences.ScreenOrientationPreferencesDao
import app.xunxun.homeclock.preferences.ShowSecondPreferencesDao
import app.xunxun.homeclock.preferences.TextColorPreferencesDao
import app.xunxun.homeclock.preferences.TextSizePreferencesDao
import app.xunxun.homeclock.preferences.TextSpaceContentPreferencesDao
import app.xunxun.homeclock.utils.FloatToast
import butterknife.BindView
import butterknife.ButterKnife
import io.github.xhinliang.lunarcalendar.LunarCalendar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.String.Companion

/**
 * Created by fengdianxun on 2017/1/19.
 */

class ClockViewController(private val activity: Activity) {

    @BindView(R.id.dateTv)
    internal var dateTv: TextView? = null
    @BindView(R.id.weekTv)
    internal var weekTv: TextView? = null
    @BindView(R.id.lunarTv)
    internal var lunarTv: TextView? = null
    @BindView(R.id.dateLl)
    internal var dateLl: LinearLayout? = null
    @BindView(R.id.timeTv)
    internal var timeTv: TextView? = null
    @BindView(R.id.ampmTv)
    internal var ampmTv: TextView? = null
    @BindView(R.id.activity_main)
    internal var activityMain: RelativeLayout? = null
    @BindView(R.id.rootFl)
    internal var rootFl: FrameLayout? = null
    @BindView(R.id.batteryTv)
    internal var batteryTv: TextView? = null
    @BindView(R.id.textSpaceTv)
    internal var textSpaceTv: TextView? = null
    @BindView(R.id.timeLl)
    internal var timeLl: LinearLayout? = null
    @BindView(R.id.centerRl)
    internal var centerRl: RelativeLayout? = null
    @BindView(R.id.battery2Tv)
    internal var battery2Tv: TextView? = null
    @BindView(R.id.backIv)
    internal var backIv: ImageView? = null
    @BindView(R.id.focusTimeTv)
    internal var focusTimeTv: TextView? = null


    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var handler: Handler? = null
    private var navigationBarIsVisible: Boolean = false
    private var isUsefullClick: Boolean = false
    private var batteryChangeReceiver: BatteryChangeReceiver? = null
    private var backgroundColor: Int = 0
    private var foregroundColor: Int = 0
    private var vibrator: Vibrator? = null
    private var lastTime: Long = 0
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var currentLight: Int = 0 // 当前屏幕的亮度
    private var updateHelper: UpdateHelper? = null
    private var gestureDetector: GestureDetector? = null
    private var toast: FloatToast? = null
    private var soundPoolHelper: SoundPoolHelper? = null

    private val isHaveWriteSettinsPermisson: Boolean
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.System.canWrite(activity)
        } else {
            true
        }

    fun onCreate(savedInstanceState: Bundle?) {
        if (ScreenOrientationPreferencesDao.get(activity) == ActivityInfo.SCREEN_ORIENTATION_SENSOR)
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        else if (ScreenOrientationPreferencesDao.get(activity) == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        else if (ScreenOrientationPreferencesDao.get(activity) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        hideNavigationBar()
        activity.setContentView(R.layout.activity_main)
        ButterKnife.bind(this, activity)
        initTypeFace()


        handler = MyHandler()
        initListner()
        init()
        vibrator = activity.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        if (LockScreenShowOnPreferencesDao.get(activity)) {
            MyService.startService(activity)
        }
        val display = activity.windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels
        updateHelper = UpdateHelper(activity)
        updateHelper!!.check(false)
        gestureDetector = GestureDetector(activity, MyGestureListener())
        val flag = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_SETTINGS)
        if (flag != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_SETTINGS), 101)
        }

        toast = FloatToast()

        soundPoolHelper = SoundPoolHelper(activity)
        soundPoolHelper!!.load()
    }


    /**
     * 说话.
     */
    private fun speak(filename: String) {

        if (EnableVibrateWholeTimePreferencesDao.get(activity)) {
            vibrator!!.vibrate(1000)
        }
        if (EnableVoiceWholeTimePreferencesDao.get(activity)) {
            soundPoolHelper!!.play(filename)
        }
    }

    /**
     * 初始化监听器.
     */
    private fun initListner() {
        rootFl!!.setOnTouchListener { v, event -> gestureDetector!!.onTouchEvent(event) }
        rootFl!!.setOnClickListener { view ->
            view.requestFocus()
            if (isUsefullClick && navigationBarIsVisible) {
                Log.v("activityMain", "hideNavigationBar")
                hideNavigationBar()
                isUsefullClick = false
            }
        }
        if (Build.VERSION.SDK_INT >= 11) {
            activity.window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0) {
                    // TODO: The navigation bar is visible. Make any desired
                    // adjustments to your UI, such as showing the action bar or
                    // other navigational controls.
                    Log.v("AA", "The navigation bar is visible")
                    navigationBarIsVisible = true
                    isUsefullClick = true
                } else {
                    // TODO: The navigation bar is NOT visible. Make any desired
                    // adjustments to your UI, such as hiding the action bar or
                    // other navigational controls.
                    Log.v("AA", "The navigation bar is NOT visible")
                    navigationBarIsVisible = false
                    isUsefullClick = true

                }
            }
        }
    }

    fun onResume() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                handler!!.sendEmptyMessage(WHAT_TIME)
            }
        }
        timer!!.schedule(timerTask, 1000, 1000)
        setBackgroundColor()
        setForegroundColor()
        setDateTime()
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        batteryChangeReceiver = BatteryChangeReceiver()
        activity.registerReceiver(batteryChangeReceiver, intentFilter)
        if (EnableShakeFeedbackPreferencesDao.get(activity)) {
            shakeFeedback()
        }

        try {
            val screenMode = Settings.System.getInt(activity.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE)
            val screenBrightness = Settings.System.getInt(activity.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
            ScreenBrightnessPreferencesDao.setSysMode(activity, screenMode)
            ScreenBrightnessPreferencesDao.setSysValue(activity, screenBrightness)
            currentLight = screenBrightness
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
        }


    }

    fun onPause() {
        if (timer != null) {
            timer!!.cancel()
            timer = null

        }

        if (timerTask != null) {
            timerTask!!.cancel()
            timerTask = null
        }
        activity.unregisterReceiver(batteryChangeReceiver)
        if (EnableShakeFeedbackPreferencesDao.get(activity)) {
            PgyFeedbackShakeManager.unregister()
        }

        if (isHaveWriteSettinsPermisson) {
            setScreenMode(ScreenBrightnessPreferencesDao.getSysMode(activity))
            setScreenBrightness(ScreenBrightnessPreferencesDao.getSysValue(activity).toFloat())
        }
    }

    /**
     * 设置当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    private fun setScreenMode(value: Int) {
        Settings.System.putInt(activity.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, value)
    }

    /**
     * 设置当前屏幕亮度值 0--255，并使之生效
     */
    private fun setScreenBrightness(value: Float) {
        val mWindow = activity.window
        val mParams = mWindow.attributes
        val f = value / 255.0f
        mParams.screenBrightness = f
        mWindow.attributes = mParams

        // 保存设置的屏幕亮度值
        Settings.System.putInt(activity.contentResolver, Settings.System.SCREEN_BRIGHTNESS, value.toInt())
    }

    /**
     * 摇一摇反馈.
     */
    private fun shakeFeedback() {
        PgyFeedbackShakeManager.setShakingThreshold(1000)
        PgyFeedbackShakeManager.register(activity)

    }

    fun onDestroy() {
        PgyCrashManager.unregister()
        soundPoolHelper!!.release()

    }

    /**
     * 设置前景色.
     */
    private fun setForegroundColor() {
        val color = foregroundColor
        setForegroundColor(color)
    }

    /**
     * 设置前景色.
     *
     * @param color
     */
    private fun setForegroundColor(color: Int) {
        timeTv!!.setTextColor(color)
        dateTv!!.setTextColor(color)
        weekTv!!.setTextColor(color)
        lunarTv!!.setTextColor(color)
        ampmTv!!.setTextColor(color)
        batteryTv!!.setTextColor(color)
        battery2Tv!!.setTextColor(color)
        textSpaceTv!!.setTextColor(color)
        focusTimeTv!!.setTextColor(color)
    }

    /**
     * 设置背景色.
     */
    private fun setBackgroundColor() {
        val color = backgroundColor
        setBackgroundColor(color)
    }

    /**
     * 设置背景色.
     *
     * @param color
     */
    private fun setBackgroundColor(color: Int) {
        val mode = BackgroundModePreferencesDao.get(activity)

        if (mode == BackgroundModePreferencesDao.MODE_ONLINE_IMAGE) {
            getPic()
        } else if (mode == BackgroundModePreferencesDao.MODE_COLOR) {
            activityMain!!.setBackgroundColor(color)
        } else if (mode == BackgroundModePreferencesDao.MODE_LOCAL_IMAGE) {
            activityMain!!.setBackgroundColor(Color.argb(100, 0, 0, 0))
            val path = LocalImageFilePathPreferencesDao.get(activity)
            if (!TextUtils.isEmpty(path)) {
                val file = File(path)
                Picasso.with(activity).load(file).into(backIv)
            } else {
                activityMain!!.setBackgroundColor(color)
            }
        }

    }

    private fun getPic() {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.bing.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(Api::class.java)
        api.pic.enqueue(object : Callback<Pic> {
            override fun onResponse(call: Call<Pic>, response: Response<Pic>) {
                if (response.isSuccessful && response.body() != null && !response.body().images!!.isEmpty()) {

                    val imagesEntity = response.body().images!![0]
                    val url = imagesEntity.url

                    activityMain!!.setBackgroundColor(Color.argb(100, 0, 0, 0))
                    Picasso.with(activity).load(String.format("%s%s", "http://www.bing.com/", url)).into(backIv)

                }
            }

            override fun onFailure(call: Call<Pic>, t: Throwable) {

            }
        })
    }


    fun onKeyDown(keyCode: Int, event: KeyEvent) {
        when (keyCode) {
            KeyEvent.KEYCODE_MENU -> {
                Log.v("AA", "menu")
                trans2Settings()
            }
        }
    }

    /**
     * Detects and toggles immersive mode (also known as "hidey bar" mode).
     */
    fun hideNavigationBar() {

        val decorView = activity.window.decorView
        decorView.postDelayed({
            val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
            if (Build.VERSION.SDK_INT >= 11) {
                activity.window.decorView.systemUiVisibility = uiOptions
            }
        }, 800)
    }

    /**
     * 设置字体.
     */
    private fun initTypeFace() {

        val typeFace = Typeface.createFromAsset(activity.assets, "fonts/ds_digi.ttf")
        timeTv!!.typeface = typeFace
        dateTv!!.typeface = typeFace

    }

    /**
     * 初始化.
     */
    private fun init() {
        setTextViewVisibility(dateTv!!, IsShowDatePreferencesDao.get(activity))
        setTextViewVisibility(weekTv!!, IsShowWeekPreferencesDao.get(activity))
        setTextViewVisibility(lunarTv!!, IsShowLunarPreferencesDao.get(activity))
        setTextViewVisibility(batteryTv!!, IsShowBatteryPreferencesDao.get(activity))

        textSpaceTv!!.text = TextSpaceContentPreferencesDao.get(activity)

        backgroundColor = BackgroundColorPreferencesDao.get(activity)
        foregroundColor = TextColorPreferencesDao.get(activity)

        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            timeTv!!.textSize = TextSizePreferencesDao.get(activity).toFloat()
        } else if (activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            timeTv!!.textSize = (TextSizePreferencesDao.get(activity) * 0.7).toFloat()
        }

        batteryTv!!.textSize = (TextSizePreferencesDao.get(activity) * 0.13).toFloat()
        battery2Tv!!.textSize = (TextSizePreferencesDao.get(activity) * 0.13).toFloat()
        lunarTv!!.textSize = (TextSizePreferencesDao.get(activity) * 0.15).toFloat()
        dateTv!!.textSize = (TextSizePreferencesDao.get(activity) * 0.2).toFloat()
        weekTv!!.textSize = (TextSizePreferencesDao.get(activity) * 0.15).toFloat()
        ampmTv!!.textSize = (TextSizePreferencesDao.get(activity) * 0.5).toFloat()
        textSpaceTv!!.textSize = (TextSizePreferencesDao.get(activity) * 0.25).toFloat()
        focusTimeTv!!.textSize = (TextSizePreferencesDao.get(activity) * 0.2).toFloat()

    }

    /**
     * 设置控件的显隐.
     *
     * @param textView
     * @param isShow
     */
    private fun setTextViewVisibility(textView: TextView, isShow: Boolean) {
        textView.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    /**
     * 更新时间.
     */
    private fun setDateTime() {
        val now = Date()
        val calendar = Calendar.getInstance()
        val hour24 = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val hour12 = calendar.get(Calendar.HOUR)

        setTime(now, hour24, minute, hour12, second)

        if (dateTv != null)
            dateTv!!.text = dateSDF.format(now)
        if (weekTv != null)
            weekTv!!.text = weekSDF.format(now)

        val lunarCalendar = LunarCalendar.getInstance(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))

        lunarTv!!.text = String.format("%s月%s", lunarCalendar.lunarMonth, lunarCalendar.lunarDay)


        if (FocusTimePreferencesDao.get(activity) > 0) {
            focusTimeTv!!.text = diffDate(now, Date(FocusTimePreferencesDao.get(activity)))
            focusTimeTv!!.visibility = View.VISIBLE
        } else {
            focusTimeTv!!.visibility = View.GONE
        }
        if (isWholeTime(minute, second)) {
            val file = getFileByTime(hour24, hour12)
            speak(file)
        }


    }

    private fun diffDate(date1: Date, date2: Date): String {
        val result: String
        val diff = date2.time - date1.time
        if (diff < 0) {
            result = "「已过期」"
        } else if (diff < 1000) {
            result = "「已到达」"
        } else if (diff < 60 * 1000) {
            result = String.format("「剩余%s秒」", diff / 1000)
        } else if (diff < 60 * 1000 * 60) {
            result = String.format("「剩余%s分钟」", diff / 1000 / 60)
        } else if (diff < 60 * 1000 * 60 * 24) {
            result = String.format("「剩余%s小时」", diff / 1000 / 60 / 60)
        } else {
            result = String.format("「剩余%s天」", diff / 1000 / 60 / 60 / 24)
        }
        return result
    }

    /**
     * 到整点了.
     *
     * @return
     */
    private fun isWholeTime(minute: Int, second: Int): Boolean {
        return minute == 0 && second == 0
    }

    /**
     * 根据时间获取声音文件.
     *
     * @param hour24
     * @param hour12
     * @return
     */
    private fun getFileByTime(hour24: Int, hour12: Int): String {
        var hour24 = hour24
        var hour12 = hour12
        if (hour24 == 0) hour24 = 24
        if (hour12 == 0) hour12 = 12
        val ampm = if (hour24 > 12) "pm" else "am"
        return String.format("clock_%s%d", ampm, hour12)

    }

    /**
     * 设置时间.
     *
     * @param now
     * @param hour24
     * @param minute
     * @param hour12
     */
    private fun setTime(now: Date, hour24: Int, minute: Int, hour12: Int, second: Int) {
        if (Is12TimePreferencesDao.get(activity)) {
            if (timeTv != null) {
                val spannable = getAmPmTextSpannable(hour24, minute, hour12, second)

                timeTv!!.text = spannable
            }

        } else {
            if (timeTv != null) {
                if (ShowSecondPreferencesDao.get(activity)) {
                    timeTv!!.text = time24SDF.format(now)
                } else {
                    if (IsMaoHaoShanShuoPreferencesDao.get(activity)) {
                        if (second % 2 == 0) {
                            timeTv!!.text = time24NoSecondSDF.format(now)
                        } else {
                            timeTv!!.text = time24NoSecondNOMaoHaoSDF.format(now)
                        }
                    } else {
                        timeTv!!.text = time24NoSecondSDF.format(now)
                    }
                }
            }
        }
    }

    /**
     * 获取ampm模式应该显示的文字.
     *
     * @param hour24
     * @param minute
     * @param hour12
     * @return
     */
    private fun getAmPmTextSpannable(hour24: Int, minute: Int, hour12: Int, second: Int): Spannable {
        var hour12 = hour12
        if (hour12 == 0 && hour24 == 12) {
            hour12 = 12
        }
        val ampm = if (hour24 >= 12) "PM" else "AM"
        var time: String? = null
        var start = 5
        var end = 7
        if (ShowSecondPreferencesDao.get(activity)) {
            time = String.format("%02d:%02d:%02d%s", hour12, minute, second, ampm)
            start = 8
            end = 10

        } else {
            if (IsMaoHaoShanShuoPreferencesDao.get(activity)) {
                if (second % 2 == 0) {
                    time = String.format("%02d:%02d%s", hour12, minute, ampm)
                } else {
                    time = String.format("%02d %02d%s", hour12, minute, ampm)
                }
            } else {
                time = String.format("%02d:%02d%s", hour12, minute, ampm)

            }
            start = 5
            end = 7
        }
        val spannable = SpannableString(time)
        spannable.setSpan(RelativeSizeSpan(0.6f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(TypefaceSpan("default"), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }


    /**
     * 跳转到设置页面.
     */
    private fun trans2Settings() {
        if (activity is MainActivity) {
            SettingsActivity.start(activity, SettingsActivity.REQUEST_MAIN)
            activity.finish()

        } else if (activity is LauncherActivity) {
            SettingsActivity.start(activity, SettingsActivity.REQUEST_LAUNCHER)
            activity.finish()

        }
    }

    /**
     * 电量监听器.
     */
    inner class BatteryChangeReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (Intent.ACTION_BATTERY_CHANGED == intent.action) {
                //获取当前电量
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                //电量的总刻度
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)

                val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL


                //把它转成百分比
                if (batteryTv != null) {
                    batteryTv!!.text = String.format("%s:%d%%", if (isCharging) "充电中" else "电量", level * 100 / scale)
                    battery2Tv!!.text = String.format("%s:%d%%", if (isCharging) "充电中" else "电量", level * 100 / scale)
                }

            }
        }
    }

    private inner class MyHandler : Handler() {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                setDateTime()
                if (EnableProtectScreenPreferencesDao.get(activity) && System.currentTimeMillis() - lastTime > 1000 * 60 * 5) {
                    val params = timeLl!!.layoutParams as RelativeLayout.LayoutParams
                    val random = Random()
                    val newTopMarginMax = if (centerRl!!.height <= 0 || timeLl!!.height <= 0) screenHeight / 2 else centerRl!!.height - timeLl!!.height

                    //                    Crashlytics.setInt("newTopMarginMax", newTopMarginMax);
                    params.topMargin = if (newTopMarginMax <= 0) 0 else random.nextInt(newTopMarginMax)
                    val newLeftMarginMax = if (centerRl!!.width <= 0 || timeLl!!.width <= 0) screenWidth / 2 else centerRl!!.width - timeLl!!.width

                    //                    Crashlytics.setInt("newLeftMarginMax", newLeftMarginMax);
                    params.leftMargin = if (newLeftMarginMax <= 0) 0 else random.nextInt(newLeftMarginMax)
                    timeLl!!.layoutParams = params
                    params.addRule(RelativeLayout.CENTER_IN_PARENT, 0)
                    lastTime = System.currentTimeMillis()


                    batteryTv!!.visibility = View.GONE
                    battery2Tv!!.visibility = if (IsShowBatteryPreferencesDao.get(activity)) View.VISIBLE else View.GONE


                    val params1 = dateLl!!.layoutParams as RelativeLayout.LayoutParams
                    val align = random.nextInt(2)
                    Log.v("xxx", "params.topMargin:$align")
                    params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
                    params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
                    params1.addRule(if (align == 0) RelativeLayout.ALIGN_PARENT_LEFT else RelativeLayout.ALIGN_PARENT_RIGHT)
                    dateLl!!.layoutParams = params1
                    dateLl!!.gravity = if (align == 0) Gravity.LEFT else Gravity.RIGHT


                }

            }
        }

    }

    private inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {

            trans2Settings()
            return super.onDoubleTap(e)
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            //            if (distanceY > 30) {
            //                currentLight = (int) (currentLight + (255 - currentLight)
            //                        * distanceY / screenHeight);
            //                currentLight = currentLight > 255 ? 255 : currentLight;
            //                if (isHaveWriteSettinsPermisson()) {
            //                    setScreenBrightness(currentLight);
            //                    toast.show(activity, "亮度增加到:" + currentLight, activity.getWindow().getDecorView());
            //                }
            //            } else if (distanceY < -30) {
            //                currentLight = (int) (currentLight - currentLight
            //                        * (distanceY) / screenHeight);
            //                currentLight = currentLight < 0 ? 0 : currentLight;
            //                if (isHaveWriteSettinsPermisson()) {
            //                    setScreenBrightness(currentLight);
            //                    toast.show(activity, "亮度减弱到:" + currentLight, activity.getWindow().getDecorView());
            //                }
            //            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
    }

    companion object {

        val WHAT_TIME = 1
        private val dateSDF = SimpleDateFormat("yyyy-MM-dd")
        private val time24SDF = SimpleDateFormat("HH:mm:ss")
        private val time24NoSecondSDF = SimpleDateFormat("HH:mm")
        private val time24NoSecondNOMaoHaoSDF = SimpleDateFormat("HH mm")
        private val weekSDF = SimpleDateFormat("E")
    }
}
