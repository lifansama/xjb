package app.xunxun.homeclock.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.support.annotation.IdRes
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast

import com.fourmob.colorpicker.ColorPickerSwatch
import com.pgyersdk.feedback.PgyFeedback
import com.umeng.analytics.MobclickAgent

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

import app.xunxun.homeclock.EventNames
import app.xunxun.homeclock.MyService
import app.xunxun.homeclock.R
import app.xunxun.homeclock.helper.UpdateHelper
import app.xunxun.homeclock.preferences.BackgroundColorPreferencesDao
import app.xunxun.homeclock.preferences.BackgroundModePreferencesDao
import app.xunxun.homeclock.preferences.EnableProtectScreenPreferencesDao
import app.xunxun.homeclock.preferences.EnableVibrateWholeTimePreferencesDao
import app.xunxun.homeclock.preferences.EnableShakeFeedbackPreferencesDao
import app.xunxun.homeclock.preferences.EnableVoiceWholeTimePreferencesDao
import app.xunxun.homeclock.preferences.FocusTimePreferencesDao
import app.xunxun.homeclock.preferences.Is12TimePreferencesDao
import app.xunxun.homeclock.preferences.IsLauncherPreferencesDao
import app.xunxun.homeclock.preferences.IsMaoHaoShanShuoPreferencesDao
import app.xunxun.homeclock.preferences.IsShowBatteryPreferencesDao
import app.xunxun.homeclock.preferences.IsShowDatePreferencesDao
import app.xunxun.homeclock.preferences.IsShowLunarPreferencesDao
import app.xunxun.homeclock.preferences.IsShowWeekPreferencesDao
import app.xunxun.homeclock.preferences.KeepScreenOnPreferencesDao
import app.xunxun.homeclock.preferences.LocalImageFilePathPreferencesDao
import app.xunxun.homeclock.preferences.LockScreenShowOnPreferencesDao
import app.xunxun.homeclock.preferences.NotifyStayPreferencesDao
import app.xunxun.homeclock.preferences.ScreenOrientationPreferencesDao
import app.xunxun.homeclock.preferences.ShowSecondPreferencesDao
import app.xunxun.homeclock.preferences.TextColorPreferencesDao
import app.xunxun.homeclock.preferences.TextSizePreferencesDao
import app.xunxun.homeclock.preferences.TextSpaceContentPreferencesDao
import app.xunxun.homeclock.utils.LauncherSettings
import app.xunxun.homeclock.utils.RealPathUtil
import app.xunxun.homeclock.widget.ColorPickerDialog
import app.xunxun.homeclock.widget.DateTimePickerDialog
import app.xunxun.homeclock.widget.OnDateTimeSetListenner
import butterknife.ButterKnife
import butterknife.BindView
import io.github.xhinliang.lunarcalendar.LunarCalendar

/**
 * 设置页面.
 */
class SettingsActivity : BaseActivity() {
    @BindView(R.id.backgroundColorTv)
    internal var backgroundColorTv: TextView? = null
    @BindView(R.id.timeTv)
    internal var timeTv: TextView? = null
    @BindView(R.id.dateTv)
    internal var dateTv: TextView? = null
    @BindView(R.id.weekTv)
    internal var weekTv: TextView? = null
    @BindView(R.id.backRl)
    internal var backRl: RelativeLayout? = null
    @BindView(R.id.textColorTv)
    internal var textColorTv: TextView? = null
    @BindView(R.id.activity_settings)
    internal var activitySettings: LinearLayout? = null
    @BindView(R.id.supportTv)
    internal var supportTv: TextView? = null
    @BindView(R.id.keepScreenOnCb)
    internal var keepScreenOnCb: CheckBox? = null
    @BindView(R.id.lunarTv)
    internal var lunarTv: TextView? = null
    @BindView(R.id.ampmTv)
    internal var ampmTv: TextView? = null
    @BindView(R.id.time_12Rb)
    internal var time12Rb: RadioButton? = null
    @BindView(R.id.time_24Rb)
    internal var time24Rb: RadioButton? = null
    @BindView(R.id.timeStyleRg)
    internal var timeStyleRg: RadioGroup? = null
    @BindView(R.id.dateLl)
    internal var dateLl: LinearLayout? = null
    @BindView(R.id.setLauncherCb)
    internal var setLauncherCb: CheckBox? = null
    @BindView(R.id.showDateCb)
    internal var showDateCb: CheckBox? = null
    @BindView(R.id.showLunarCb)
    internal var showLunarCb: CheckBox? = null
    @BindView(R.id.showWeekCb)
    internal var showWeekCb: CheckBox? = null
    @BindView(R.id.batteryTv)
    internal var batteryTv: TextView? = null
    @BindView(R.id.showBatteryCb)
    internal var showBatteryCb: CheckBox? = null
    @BindView(R.id.textSpaceEt)
    internal var textSpaceEt: EditText? = null
    @BindView(R.id.feedbackTv)
    internal var feedbackTv: TextView? = null
    @BindView(R.id.enableShakeFeedbackCb)
    internal var enableShakeFeedbackCb: CheckBox? = null
    @BindView(R.id.enableSpeakWholeTimeCb)
    internal var enableVibrateWholeTimeCb: CheckBox? = null
    @BindView(R.id.enableVoiceWholeTimeCb)
    internal var enableVoiceWholeTimeCb: CheckBox? = null
    @BindView(R.id.protectScreenCb)
    internal var protectScreenCb: CheckBox? = null
    @BindView(R.id.textSizeTv)
    internal var textSizeTv: TextView? = null
    @BindView(R.id.lockScreenShowCb)
    internal var lockScreenShowCb: CheckBox? = null
    @BindView(R.id.showSecondCb)
    internal var showSecondCb: CheckBox? = null
    @BindView(R.id.backgroundColorRb)
    internal var backgroundColorRb: RadioButton? = null
    @BindView(R.id.backgroundPicRb)
    internal var backgroundPicRb: RadioButton? = null
    @BindView(R.id.localBackgroundPicRb)
    internal var localBackgroundPicRb: RadioButton? = null
    @BindView(R.id.backgroundStyleRg)
    internal var backgroundStyleRg: RadioGroup? = null
    @BindView(R.id.versionTv)
    internal var versionTv: TextView? = null
    @BindView(R.id.centerLl)
    internal var centerLl: LinearLayout? = null
    @BindView(R.id.setDateTv)
    internal var setDateTv: TextView? = null
    @BindView(R.id.maohaoShanShuoCb)
    internal var maohaoShanShuoCb: CheckBox? = null
    @BindView(R.id.sensorRb)
    internal var sensorRb: RadioButton? = null
    @BindView(R.id.landscapeRb)
    internal var landscapeRb: RadioButton? = null
    @BindView(R.id.portraitRb)
    internal var portraitRb: RadioButton? = null
    @BindView(R.id.screenOrientationRg)
    internal var screenOrientationRg: RadioGroup? = null
    @BindView(R.id.screenBrightCb)
    internal var screenBrightCb: CheckBox? = null
    @BindView(R.id.notifyStayCb)
    internal var notifyStayCb: CheckBox? = null
    private var backgroundColorPickerDialog: ColorPickerDialog? = null
    private var textColorPickerDialog: ColorPickerDialog? = null
    private val dateSDF = SimpleDateFormat("yyyy-MM-dd")
    private val time12SDF = SimpleDateFormat("hh:mm:ss")
    private val time12NoSecondSDF = SimpleDateFormat("hh:mm")
    private val time24SDF = SimpleDateFormat("HH:mm:ss")
    private val time24NoSecondSDF = SimpleDateFormat("HH:mm")
    private val weekSDF = SimpleDateFormat("E")
    private var colors: IntArray? = null
    private var dateTimePickerDialog: DateTimePickerDialog? = null
    private var updateHelper: UpdateHelper? = null
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        ButterKnife.bind(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        backgroundColorPickerDialog = ColorPickerDialog(this)
        colors = resources.getIntArray(R.array.colors)
        textColorPickerDialog = ColorPickerDialog(this)
        textColorPickerDialog!!.initialize(R.string.txt_select_color, colors!!, TextColorPreferencesDao.get(this), 4, 2)

        protectScreenCb!!.isChecked = EnableProtectScreenPreferencesDao.get(this)

        maohaoShanShuoCb!!.isChecked = IsMaoHaoShanShuoPreferencesDao.get(this)
        dateTimePickerDialog = DateTimePickerDialog(this)
        dateTimePickerDialog!!.setOnDateTimeSetListenner(object:OnDateTimeSetListenner{
            override fun onDateTimeSeted(date: Date?) {

                if (date != null) {
                    FocusTimePreferencesDao.set(this@SettingsActivity, date.time)

                    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
                    setDateTv!!.text = String.format("到期时间:%s", simpleDateFormat.format(date))
                } else {
                    FocusTimePreferencesDao.set(this@SettingsActivity, 0)
                    setDateTv!!.text = "点击设置到期时间"
                }
            }
        })

        val mode = BackgroundModePreferencesDao.get(this)
        if (mode == BackgroundModePreferencesDao.MODE_COLOR) {
            backgroundColorRb!!.isChecked = true

        } else if (mode == BackgroundModePreferencesDao.MODE_ONLINE_IMAGE) {
            backgroundPicRb!!.isChecked = true
        } else if (mode == BackgroundModePreferencesDao.MODE_LOCAL_IMAGE) {
            localBackgroundPicRb!!.isChecked = true
        }
        initListener()

        init()
        if (FocusTimePreferencesDao.get(this@SettingsActivity) > 0) {
            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
            setDateTv!!.text = String.format("到期时间:%s", simpleDateFormat.format(Date(FocusTimePreferencesDao.get(this@SettingsActivity))))
        } else {
            setDateTv!!.text = "点击设置到期时间"
        }

        updateHelper = UpdateHelper(this)
        countDownTimer = MyCountDown((60 * 1000).toLong(), 1000)
    }

    /**
     * 设置监听器.
     */
    private fun initListener() {
        backgroundColorTv!!.setOnClickListener { backgroundColorPickerDialog!!.show() }
        backgroundColorPickerDialog!!.initialize(R.string.txt_select_color, colors!!, BackgroundColorPreferencesDao.get(this), 4, 2)
        backgroundColorPickerDialog!!.setOnColorSelectedListener(object:ColorPickerSwatch.OnColorSelectedListener{
            override fun onColorSelected(color: Int) {
                backRl!!.setBackgroundColor(color)
                backgroundColorRb!!.isChecked = true
                BackgroundColorPreferencesDao.set(this@SettingsActivity, color)
                MobclickAgent.onEvent(this@SettingsActivity, EventNames.EVENT_CHANGE_BACKGROUND_COLOR)
            }
        })
        textColorPickerDialog!!.setOnColorSelectedListener(object:ColorPickerSwatch.OnColorSelectedListener{
            override fun onColorSelected(color: Int) {
                TextColorPreferencesDao.set(this@SettingsActivity, color)
                setForegroundColor()
                MobclickAgent.onEvent(this@SettingsActivity, EventNames.EVENT_CHANGE_TEXT_COLOR)
            }
        })
        textColorTv!!.setOnClickListener { textColorPickerDialog!!.show() }
        textSizeTv!!.setOnClickListener { }

        supportTv!!.setOnClickListener { view -> SupportActivity.start(view.context) }
        keepScreenOnCb!!.setOnCheckedChangeListener { compoundButton, isCheck -> KeepScreenOnPreferencesDao.set(compoundButton.context, isCheck) }
        keepScreenOnCb!!.isChecked = KeepScreenOnPreferencesDao.get(this)


        timeStyleRg!!.setOnCheckedChangeListener { radioGroup, id ->
            Is12TimePreferencesDao.set(radioGroup.context, id == R.id.time_12Rb)
            setTime()
        }

        setLauncherCb!!.isChecked = IsLauncherPreferencesDao.get(this)
        setLauncherCb!!.setOnCheckedChangeListener { compoundButton, isCheck ->
            IsLauncherPreferencesDao.set(compoundButton.context, isCheck)


            LauncherSettings.setLauncher(compoundButton.context, isCheck)

            if (isCheck) {
                val selector = Intent(Intent.ACTION_MAIN)
                selector.addCategory(Intent.CATEGORY_HOME)
                compoundButton.context.startActivity(selector)
            }
        }
        showDateCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            IsShowDatePreferencesDao.set(buttonView.context, isChecked)
            setShowDateCb(isChecked)
        }
        showLunarCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            IsShowLunarPreferencesDao.set(buttonView.context, isChecked)
            setShowLunarCb(isChecked)
        }
        showWeekCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            IsShowWeekPreferencesDao.set(buttonView.context, isChecked)
            setShowWeekCb(isChecked)
        }
        showBatteryCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            IsShowBatteryPreferencesDao.set(buttonView.context, isChecked)
            batteryTv!!.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        feedbackTv!!.setOnClickListener { PgyFeedback.getInstance().showDialog(this@SettingsActivity) }
        enableShakeFeedbackCb!!.setOnCheckedChangeListener { buttonView, isChecked -> EnableShakeFeedbackPreferencesDao.set(buttonView.context, isChecked) }
        enableVibrateWholeTimeCb!!.setOnCheckedChangeListener { buttonView, isChecked -> EnableVibrateWholeTimePreferencesDao.set(buttonView.context, isChecked) }
        enableVoiceWholeTimeCb!!.setOnCheckedChangeListener { buttonView, isChecked -> EnableVoiceWholeTimePreferencesDao.set(buttonView.context, isChecked) }
        protectScreenCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            EnableProtectScreenPreferencesDao.set(buttonView.context, isChecked)
            if (isChecked)
                showAlert("开启防烧屏后文字会5分钟换一次位置，如果字太大影响移动后的显示请自行调小。")
        }
        textSizeTv!!.setOnClickListener {
            val linearLayout = LinearLayout(this@SettingsActivity)
            linearLayout.setPadding(0, 64, 0, 64)
            linearLayout.orientation = LinearLayout.VERTICAL
            val seekBar = SeekBar(this@SettingsActivity)
            seekBar.max = 300
            val textView = TextView(this@SettingsActivity)
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                    textView.text = seekBar.progress.toString()
                    TextSizePreferencesDao.set(this@SettingsActivity, seekBar.progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {

                }
            })
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.topMargin = 16
            seekBar.layoutParams = params

            textView.gravity = Gravity.CENTER
            textView.textSize = 32f
            textView.setTextColor(Color.BLACK)
            textView.text = TextSizePreferencesDao.get(this@SettingsActivity).toString()
            seekBar.progress = TextSizePreferencesDao.get(this@SettingsActivity)
            linearLayout.addView(textView)
            linearLayout.addView(seekBar)
            AlertDialog.Builder(this@SettingsActivity).setView(linearLayout).show()
        }
        lockScreenShowCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            LockScreenShowOnPreferencesDao.set(buttonView.context, isChecked)
            if (isChecked) {
                MyService.startService(this@SettingsActivity)
            } else {
                MyService.stopService(this@SettingsActivity)
            }
        }
        showSecondCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            ShowSecondPreferencesDao.set(buttonView.context, isChecked)
            setTime()
        }
        backgroundStyleRg!!.setOnCheckedChangeListener { group, checkedId ->
            val rb = findViewById(checkedId) as RadioButton
            BackgroundModePreferencesDao.set(group.context, Integer.parseInt(rb.tag as String))

            if (checkedId == R.id.backgroundPicRb) {
                showAlert("背景图片一天一换")
            } else if (checkedId == R.id.localBackgroundPicRb) {
                openGallery()
            }
        }
        versionTv!!.setOnClickListener { updateHelper!!.check(true) }
        setDateTv!!.setOnClickListener { dateTimePickerDialog!!.show() }
        maohaoShanShuoCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            IsMaoHaoShanShuoPreferencesDao.set(buttonView.context, isChecked)
            if (isChecked) {
                showAlert("冒号闪烁需要在不显示秒针时起作用。")
            }
        }
        if (ScreenOrientationPreferencesDao.get(this) == ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
            sensorRb!!.isChecked = true
        } else if (ScreenOrientationPreferencesDao.get(this) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            landscapeRb!!.isChecked = true
        } else if (ScreenOrientationPreferencesDao.get(this) == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            portraitRb!!.isChecked = true
        }
        screenOrientationRg!!.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.sensorRb) {
                ScreenOrientationPreferencesDao.set(group.context, ActivityInfo.SCREEN_ORIENTATION_SENSOR)

            } else if (checkedId == R.id.landscapeRb) {
                ScreenOrientationPreferencesDao.set(group.context, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

            } else if (checkedId == R.id.portraitRb) {
                ScreenOrientationPreferencesDao.set(group.context, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

            }
        }
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //            screenBrightCb.setVisibility(View.VISIBLE);
        //            screenBrightCb.setChecked(Settings.System.canWrite(this));
        //        } else {
        //            screenBrightCb.setVisibility(View.GONE);
        //        }
        //        screenBrightCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        //            @Override
        //            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //                    if (!Settings.System.canWrite(buttonView.getContext()) && isChecked) {
        //
        //                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        //                        builder.setTitle("温馨提醒");
        //                        builder.setMessage("这个功能需要修改系统设置的权限，请给授权！");
        //                        builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
        //                            @Override
        //                            public void onClick(DialogInterface dialog, int which) {
        //
        //                                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        //                                intent.setData(Uri.parse("package:" + getPackageName()));
        //                                startActivityForResult(intent, 100);
        //                            }
        //                        });
        //                        builder.show();
        //
        //                    }
        //                }
        //            }
        //        });
        screenBrightCb!!.visibility = View.GONE
        notifyStayCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            NotifyStayPreferencesDao.set(buttonView.context, isChecked)
            if (isChecked) {
                MyService.startService(buttonView.context)
            } else {
                MyService.stopService(buttonView.context)

            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(this)) {
                    screenBrightCb!!.isChecked = false
                }
            }
        } else if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data.data
                val path = RealPathUtil.getRealPathFromURI(this, uri)
                //                File newFile = Compressor.getDefault(this).compressToFile(new File(path));
                LocalImageFilePathPreferencesDao.set(this, path!!)
                Toast.makeText(this, "选图成功，后退查看效果", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAlert(msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("温馨提醒")
        builder.setMessage(msg)
        builder.setPositiveButton("知道了", null)
        builder.show()

    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        val intent = Intent.createChooser(galleryIntent, "选择图片")
        startActivityForResult(intent, 200)
    }


    /**
     * 初始化设置.
     */
    private fun init() {
        setBackgroundColor()
        setForegroundColor()
        setDate()
        if (Is12TimePreferencesDao.get(this))
            time12Rb!!.isChecked = true
        else
            time24Rb!!.isChecked = true

        setTime()
        LauncherSettings.setLauncher(this, IsLauncherPreferencesDao.get(this))
        setShowDateCb(IsShowDatePreferencesDao.get(this))
        setShowLunarCb(IsShowLunarPreferencesDao.get(this))
        setShowWeekCb(IsShowWeekPreferencesDao.get(this))
        batteryTv!!.visibility = if (IsShowBatteryPreferencesDao.get(this)) View.VISIBLE else View.GONE
        if (!TextUtils.isEmpty(TextSpaceContentPreferencesDao.get(this))) {
            textSpaceEt!!.setText(TextSpaceContentPreferencesDao.get(this))
        }
        textSpaceEt!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                TextSpaceContentPreferencesDao.set(this@SettingsActivity, s.toString())

            }
        })
        enableShakeFeedbackCb!!.isChecked = EnableShakeFeedbackPreferencesDao.get(this)
        enableVibrateWholeTimeCb!!.isChecked = EnableVibrateWholeTimePreferencesDao.get(this)
        enableVoiceWholeTimeCb!!.isChecked = EnableVibrateWholeTimePreferencesDao.get(this)
        showBatteryCb!!.isChecked = IsShowBatteryPreferencesDao.get(this)

        lockScreenShowCb!!.isChecked = LockScreenShowOnPreferencesDao.get(this)
        showSecondCb!!.isChecked = ShowSecondPreferencesDao.get(this)
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT)
            versionTv!!.text = String.format("检查更新(v%s)", packageInfo.versionName)

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        notifyStayCb!!.isChecked = NotifyStayPreferencesDao.get(this)

    }

    /**
     * 设置当前日期.
     */
    private fun setDate() {
        val now = Date()
        dateTv!!.text = dateSDF.format(now)
        weekTv!!.text = weekSDF.format(now)
        val calendar = Calendar.getInstance()
        val lunarCalendar = LunarCalendar.getInstance(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
        lunarTv!!.text = String.format("%s月%s日", lunarCalendar.lunarMonth, lunarCalendar.lunarDay)
    }

    private fun setShowDateCb(isShow: Boolean) {
        showDateCb!!.isChecked = isShow
        dateTv!!.visibility = if (isShow) View.VISIBLE else View.GONE

    }

    private fun setShowWeekCb(isShow: Boolean) {

        weekTv!!.visibility = if (isShow) View.VISIBLE else View.GONE
        showWeekCb!!.isChecked = isShow
    }

    private fun setShowLunarCb(isShow: Boolean) {
        showLunarCb!!.isChecked = isShow
        lunarTv!!.visibility = if (isShow) View.VISIBLE else View.GONE

    }


    /**
     * 设置时间.
     */
    private fun setTime() {
        val now = Date()
        val timeStr: String
        if (Is12TimePreferencesDao.get(this)) {
            val calendar = Calendar.getInstance()
            if (ShowSecondPreferencesDao.get(this)) {
                timeStr = time12SDF.format(now)
            } else {
                timeStr = time12NoSecondSDF.format(now)
            }
            if (calendar.get(Calendar.HOUR_OF_DAY) >= 12) {
                ampmTv!!.text = "PM"

            } else {
                ampmTv!!.text = "AM"

            }
            ampmTv!!.visibility = View.VISIBLE
        } else {
            if (ShowSecondPreferencesDao.get(this)) {
                timeStr = time24SDF.format(now)
            } else {
                timeStr = time24NoSecondSDF.format(now)
            }
            ampmTv!!.visibility = View.GONE

        }
        timeTv!!.text = timeStr
    }

    /**
     * 设置前景色.
     */
    private fun setForegroundColor() {
        val color = TextColorPreferencesDao.get(this)
        timeTv!!.setTextColor(color)
        dateTv!!.setTextColor(color)
        weekTv!!.setTextColor(color)
        ampmTv!!.setTextColor(color)
        lunarTv!!.setTextColor(color)
        batteryTv!!.setTextColor(color)
        textSpaceEt!!.setTextColor(color)
        setDateTv!!.setTextColor(color)
    }

    /**
     * 设置背景色.
     */
    private fun setBackgroundColor() {
        backRl!!.setBackgroundColor(BackgroundColorPreferencesDao.get(this))
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        } else if (item.itemId == R.id.action_support) {
            SupportActivity.start(this)
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
        countDownTimer!!.start()
    }

    public override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
        countDownTimer!!.cancel()
    }

    override fun onBackPressed() {
        if (IsLauncherPreferencesDao.get(this)) {
            val requestCode = intent.getIntExtra(REQUEST_CODE, -1)
            if (requestCode == REQUEST_MAIN) {
                MainActivity.start(this)
                finish()
            } else if (requestCode == REQUEST_LAUNCHER) {
                LauncherActivity.start(this)
                finish()
            } else {
                finish()
            }

        } else {
            MainActivity.start(this)
            finish()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    internal inner class MyCountDown
    /**
     * @param millisInFuture    The number of millis in the future from the call
     * to [.start] until the countdown is done and [.onFinish]
     * is called.
     * @param countDownInterval The interval along the way to receive
     * [.onTick] callbacks.
     */
    (millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            title = String.format("设置(%s秒)", millisUntilFinished / 1000)

        }

        override fun onFinish() {
            onBackPressed()

        }
    }

    companion object {
        val REQUEST_CODE = "requestCode"
        val REQUEST_MAIN = 1
        val REQUEST_LAUNCHER = 2

        fun start(context: Context, requestCode: Int) {
            val intent = Intent(context, SettingsActivity::class.java)
            intent.putExtra(REQUEST_CODE, requestCode)
            context.startActivity(intent)
        }

        fun startNewTask(context: Context, requestCode: Int) {
            val intent = Intent(context, SettingsActivity::class.java)
            intent.putExtra(REQUEST_CODE, requestCode)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        }
    }

}
