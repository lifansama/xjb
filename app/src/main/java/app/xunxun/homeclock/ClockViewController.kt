package app.xunxun.homeclock

//import com.crashlytics.android.Crashlytics;

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
import android.os.*
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
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import app.xunxun.homeclock.activity.LauncherActivity
import app.xunxun.homeclock.activity.MainActivity
import app.xunxun.homeclock.activity.SettingsActivity
import app.xunxun.homeclock.api.PicApi
import app.xunxun.homeclock.api.wea
import app.xunxun.homeclock.dao.WeatherDao
import app.xunxun.homeclock.helper.SoundPoolHelper
import app.xunxun.homeclock.helper.WeatherHelper
import app.xunxun.homeclock.model.Pic
import app.xunxun.homeclock.pref.MODE_COLOR
import app.xunxun.homeclock.pref.MODE_LOCAL_IMAGE
import app.xunxun.homeclock.pref.MODE_ONLINE_IMAGE
import app.xunxun.homeclock.pref.SimplePref
import app.xunxun.homeclock.utils.FloatToast
import com.squareup.picasso.Picasso
import io.github.xhinliang.lunarcalendar.LunarCalendar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.textColor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by fengdianxun on 2017/1/19.
 */

class ClockViewController(private val activity: Activity) {


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
    private var gestureDetector: GestureDetector? = null
    private var toast: FloatToast? = null
    private var soundPoolHelper: SoundPoolHelper? = null
    /**
     * 最后更新天气的时间.
     */
    private var latestWeatherUpdateTime: Long = 0

    private val isHaveWriteSettinsPermisson: Boolean
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.System.canWrite(activity)
        } else {
            true
        }

    fun onCreate(savedInstanceState: Bundle?) {
        if (SimplePref.create(activity).screenOrientation().get() == ActivityInfo.SCREEN_ORIENTATION_SENSOR)
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        else if (SimplePref.create(activity).screenOrientation().get() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        else if (SimplePref.create(activity).screenOrientation().get() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        hideNavigationBar()
        activity.setContentView(R.layout.activity_main)
//        ButterKnife.bind(this, activity)
        initTypeFace()


        handler = MyHandler()
        initListner()
        init()
        vibrator = activity.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        if (SimplePref.create(activity).lockScreenShowOn().get()) {
            MyService.startService(activity)
        }
        val display = activity.windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels
        gestureDetector = GestureDetector(activity, MyGestureListener())
        val flag = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (flag != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101)
        }

        toast = FloatToast()

        soundPoolHelper = SoundPoolHelper(activity)
        soundPoolHelper!!.load()
        loadWeather()
    }

    fun loadWeather() {
        val cityNum = SimplePref.create(activity).city().get()
        if (cityNum.isNotEmpty())
            async(UI) {
                try {
                    val weatherHelper = WeatherHelper()
                    val weather = weatherHelper.weather(cityNum)
                    weather?.let {
                        val temperature = it.current.temperature
                        val wea = it.current.wea(activity)
                        val city = WeatherDao.city(activity, cityNum)
                        val text = "${city?.name} | $wea | ${temperature.value}${temperature.unit}"
                        Log.v("text", text)
                        activity.weatherTv.text = text
                        latestWeatherUpdateTime = System.currentTimeMillis()
                    }
                    Log.v("weather", weather.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
    }


    /**
     * 说话.
     */
    private fun speak(filename: String) {

        if (SimplePref.create(activity).enableVibrateWholeTime().get()) {
            vibrator!!.vibrate(1000)
        }
        if (SimplePref.create(activity).enableVoiceWholeTime().get()) {
            soundPoolHelper!!.play(filename)
        }
    }

    /**
     * 初始化监听器.
     */
    private fun initListner() {
        activity.rootFl!!.setOnTouchListener { v, event -> gestureDetector!!.onTouchEvent(event) }
        activity.rootFl!!.setOnClickListener { view ->
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
        try {
            activity.unregisterReceiver(batteryChangeReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
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

    fun onDestroy() {
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
        activity.timeTv!!.setTextColor(color)
        activity.dateTv!!.setTextColor(color)
        activity.weekTv!!.setTextColor(color)
        activity.lunarTv!!.setTextColor(color)
        activity.ampmTv!!.setTextColor(color)
        activity.batteryTv!!.setTextColor(color)
        activity.battery2Tv!!.setTextColor(color)
        activity.textSpaceTv!!.setTextColor(color)
        activity.focusTimeTv!!.setTextColor(color)
        activity.weatherTv.textColor = color
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
        val mode = SimplePref.create(activity).backgroundMode().get()

        if (mode == MODE_ONLINE_IMAGE) {
            getPic()
        } else if (mode == MODE_COLOR) {
            activity.activity_main!!.setBackgroundColor(color)
        } else if (mode == MODE_LOCAL_IMAGE) {
            activity.activity_main!!.setBackgroundColor(Color.argb(100, 0, 0, 0))
            val path = SimplePref.create(activity).localImageFilePath().get()
            if (!TextUtils.isEmpty(path)) {
                val file = File(path)
                Picasso.with(activity).load(file).into(activity.backIv)
            } else {
                activity.activity_main!!.setBackgroundColor(color)
            }
        }

    }

    private fun getPic() {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.bing.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(PicApi::class.java)
        api.pic.enqueue(object : Callback<Pic> {
            override fun onResponse(call: Call<Pic>, response: Response<Pic>) {
                if (response.isSuccessful && response.body() != null) {
                    val resp = response.body()
                    resp?.images?.let {
                        if (!it.isEmpty()) {

                            val imagesEntity = it[0]
                            val url = imagesEntity.url

                            activity.activity_main!!.setBackgroundColor(Color.argb(100, 0, 0, 0))
                            Picasso.with(activity).load(String.format("%s%s", "http://www.bing.com/", url)).into(activity.backIv)
                        }

                    }


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
        activity.timeTv!!.typeface = typeFace
        activity.dateTv!!.typeface = typeFace

    }

    /**
     * 初始化.
     */
    private fun init() {
        setTextViewVisibility(activity.dateTv!!, SimplePref.create(activity).isShowDate().get())
        setTextViewVisibility(activity.weekTv!!, SimplePref.create(activity).isShowWeek().get())
        setTextViewVisibility(activity.lunarTv!!, SimplePref.create(activity).isShowLunar().get())
        setTextViewVisibility(activity.batteryTv!!, SimplePref.create(activity).isShowBattery().get())

        activity.textSpaceTv!!.text = SimplePref.create(activity).textSpaceContent().get()

        backgroundColor = SimplePref.create(activity).backgroundColor().get()
        foregroundColor = SimplePref.create(activity).textColor().get()

        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            activity.timeTv!!.textSize = SimplePref.create(activity).textSize().get().toFloat()
        } else if (activity.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            activity.timeTv!!.textSize = (SimplePref.create(activity).textSize().get() * 0.7).toFloat()
        }

        activity.batteryTv!!.textSize = (SimplePref.create(activity).textSize().get() * 0.13).toFloat()
        activity.battery2Tv!!.textSize = (SimplePref.create(activity).textSize().get() * 0.13).toFloat()
        activity.lunarTv!!.textSize = (SimplePref.create(activity).textSize().get() * 0.15).toFloat()
        activity.weatherTv!!.textSize = (SimplePref.create(activity).textSize().get() * 0.15).toFloat()
        activity.dateTv!!.textSize = (SimplePref.create(activity).textSize().get() * 0.2).toFloat()
        activity.weekTv!!.textSize = (SimplePref.create(activity).textSize().get() * 0.15).toFloat()
        activity.ampmTv!!.textSize = (SimplePref.create(activity).textSize().get() * 0.5).toFloat()
        activity.textSpaceTv!!.textSize = (SimplePref.create(activity).textSize().get() * 0.25).toFloat()
        activity.focusTimeTv!!.textSize = (SimplePref.create(activity).textSize().get() * 0.2).toFloat()

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

        if (activity.dateTv != null)
            activity.dateTv!!.text = dateSDF.format(now)
        if (activity.weekTv != null)
            activity.weekTv!!.text = weekSDF.format(now)

        val lunarCalendar = LunarCalendar.getInstance(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))

        activity.lunarTv!!.text = String.format("%s月%s", lunarCalendar.lunarMonth, lunarCalendar.lunarDay)


        if (SimplePref.create(activity).focusTime().get() > 0) {
            activity.focusTimeTv!!.text = diffDate(now, Date(SimplePref.create(activity).focusTime().get()))
            activity.focusTimeTv!!.visibility = View.VISIBLE
        } else {
            activity.focusTimeTv!!.visibility = View.GONE
        }
        if (isWholeTime(minute, second)) {
            val file = getFileByTime(hour24, hour12, minute)
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
     * 到整点或者半点了.
     *
     * @return
     */
    private fun isWholeTime(minute: Int, second: Int): Boolean {
        return (minute == 0 || minute == 30) && second == 0
    }

    /**
     * 根据时间获取声音文件.
     *
     * @param hour24
     * @param hour12
     * @return
     */
    private fun getFileByTime(hour24: Int, hour12: Int, minute: Int): String {
        var hour24 = hour24
        var hour12 = hour12
        if (hour24 == 0) hour24 = 24
        if (hour12 == 0) hour12 = 12
        val ampm = if (hour24 > 12) "pm" else "am"
        val minuteStr = if (minute == 30) "_30" else ""
        return String.format("clock_%s%d%s", ampm, hour12, minuteStr)

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
        if (SimplePref.create(activity).is12Time().get()) {
            if (activity.timeTv != null) {
                val spannable = getAmPmTextSpannable(hour24, minute, hour12, second)

                activity.timeTv!!.text = spannable
            }

        } else {
            if (activity.timeTv != null) {
                if (SimplePref.create(activity).showSecond().get()) {
                    activity.timeTv!!.text = time24SDF.format(now)
                } else {
                    if (SimplePref.create(activity).isMaoHaoShanShuo().get()) {
                        if (second % 2 == 0) {
                            activity.timeTv!!.text = time24NoSecondSDF.format(now)
                        } else {
                            activity.timeTv!!.text = time24NoSecondNOMaoHaoSDF.format(now)
                        }
                    } else {
                        activity.timeTv!!.text = time24NoSecondSDF.format(now)
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
        if (SimplePref.create(activity).showSecond().get()) {
            time = String.format("%02d:%02d:%02d%s", hour12, minute, second, ampm)
            start = 8
            end = 10

        } else {
            if (SimplePref.create(activity).isMaoHaoShanShuo().get()) {
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
                if (activity.batteryTv != null) {
                    activity.batteryTv!!.text = String.format("%s:%d%%", if (isCharging) "充电中" else "电量", level * 100 / scale)
                    activity.battery2Tv!!.text = String.format("%s:%d%%", if (isCharging) "充电中" else "电量", level * 100 / scale)
                }

            }
        }
    }

    private inner class MyHandler : Handler() {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                setDateTime()

                //查看是否1显示没更新天气了
                if (System.currentTimeMillis() - latestWeatherUpdateTime > 1000 * 60 * 60) {
                    loadWeather()
                }
                if (SimplePref.create(activity).enableProtectScreen().get() && System.currentTimeMillis() - lastTime > 1000 * 60 * 5) {
                    val params = activity.timeLl!!.layoutParams as RelativeLayout.LayoutParams
                    val random = Random()
                    val newTopMarginMax = if (activity.centerRl!!.height <= 0 || activity.timeLl!!.height <= 0) screenHeight / 2 else activity.centerRl!!.height - activity.timeLl!!.height

                    //                    Crashlytics.setInt("newTopMarginMax", newTopMarginMax);
                    params.topMargin = if (newTopMarginMax <= 0) 0 else random.nextInt(newTopMarginMax)
                    val newLeftMarginMax = if (activity.centerRl!!.width <= 0 || activity.timeLl!!.width <= 0) screenWidth / 2 else activity.centerRl!!.width - activity.timeLl!!.width

                    //                    Crashlytics.setInt("newLeftMarginMax", newLeftMarginMax);
                    params.leftMargin = if (newLeftMarginMax <= 0) 0 else random.nextInt(newLeftMarginMax)
                    activity.timeLl!!.layoutParams = params
                    params.addRule(RelativeLayout.CENTER_IN_PARENT, 0)
                    lastTime = System.currentTimeMillis()


                    activity.batteryTv!!.visibility = View.GONE
                    activity.battery2Tv!!.visibility = if (SimplePref.create(activity).isShowBattery().get()) View.VISIBLE else View.GONE


                    val params1 = activity.dateLl!!.layoutParams as RelativeLayout.LayoutParams
                    val align = random.nextInt(2)
                    Log.v("xxx", "params.topMargin:$align")
                    params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
                    params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
                    params1.addRule(if (align == 0) RelativeLayout.ALIGN_PARENT_LEFT else RelativeLayout.ALIGN_PARENT_RIGHT)
                    activity.dateLl!!.layoutParams = params1
                    activity.dateLl!!.gravity = if (align == 0) Gravity.LEFT else Gravity.RIGHT


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
