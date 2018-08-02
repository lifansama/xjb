package app.xunxun.homeclock.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import app.xunxun.homeclock.MyService
import app.xunxun.homeclock.R
import app.xunxun.homeclock.preferences.*
import app.xunxun.homeclock.utils.LauncherSettings
import app.xunxun.homeclock.widget.DateTimePickerDialog
import app.xunxun.homeclock.widget.OnDateTimeSetListenner
import kotlinx.android.synthetic.main.activity_func.*
import java.text.SimpleDateFormat
import java.util.*

class FuncActivity : BaseActivity() {
    private var dateTimePickerDialog: DateTimePickerDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_func)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        protectScreenCb!!.isChecked = EnableProtectScreenPreferencesDao.get(this)

        dateTimePickerDialog = DateTimePickerDialog(this)
        dateTimePickerDialog!!.setOnDateTimeSetListenner(object : OnDateTimeSetListenner {
            override fun onDateTimeSeted(date: Date?) {

                if (date != null) {
                    FocusTimePreferencesDao.set(this@FuncActivity, date.time)

                    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
                    setDateTv!!.text = String.format("到期时间:%s", simpleDateFormat.format(date))
                } else {
                    FocusTimePreferencesDao.set(this@FuncActivity, 0)
                    setDateTv!!.text = "点击设置到期时间"
                }
            }
        })

        if (FocusTimePreferencesDao.get(this@FuncActivity) > 0) {
            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
            setDateTv!!.text = String.format("到期时间:%s", simpleDateFormat.format(Date(FocusTimePreferencesDao.get(this@FuncActivity))))
        } else {
            setDateTv!!.text = "点击设置到期时间"
        }

        keepScreenOnCb!!.setOnCheckedChangeListener { compoundButton, isCheck -> KeepScreenOnPreferencesDao.set(compoundButton.context, isCheck) }
        keepScreenOnCb!!.isChecked = KeepScreenOnPreferencesDao.get(this)



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
        enableSpeakWholeTimeCb!!.setOnCheckedChangeListener { buttonView, isChecked -> EnableVibrateWholeTimePreferencesDao.set(buttonView.context, isChecked) }
        enableVoiceWholeTimeCb!!.setOnCheckedChangeListener { buttonView, isChecked -> EnableVoiceWholeTimePreferencesDao.set(buttonView.context, isChecked) }
        protectScreenCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            EnableProtectScreenPreferencesDao.set(buttonView.context, isChecked)
            if (isChecked)
                showAlert("开启防烧屏后文字会5分钟换一次位置，如果字太大影响移动后的显示请自行调小。")
        }
        lockScreenShowCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            LockScreenShowOnPreferencesDao.set(buttonView.context, isChecked)
            if (isChecked) {
                MyService.startService(this@FuncActivity)
            } else {
                MyService.stopService(this@FuncActivity)
            }
        }
        setDateTv!!.setOnClickListener { dateTimePickerDialog!!.show() }
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
        screenBrightCb!!.visibility = View.GONE
        notifyStayCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            NotifyStayPreferencesDao.set(buttonView.context, isChecked)
            if (isChecked) {
                MyService.startService(buttonView.context)
            } else {
                MyService.stopService(buttonView.context)

            }
        }
        enableSpeakWholeTimeCb!!.isChecked = EnableVibrateWholeTimePreferencesDao.get(this)
        enableVoiceWholeTimeCb!!.isChecked = EnableVibrateWholeTimePreferencesDao.get(this)
        lockScreenShowCb!!.isChecked = LockScreenShowOnPreferencesDao.get(this)
        notifyStayCb!!.isChecked = NotifyStayPreferencesDao.get(this)
        LauncherSettings.setLauncher(this, IsLauncherPreferencesDao.get(this))
        if (!TextUtils.isEmpty(TextSpaceContentPreferencesDao.get(this))) {
            textSpaceEt!!.setText(TextSpaceContentPreferencesDao.get(this))
        }
        textSpaceEt!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                TextSpaceContentPreferencesDao.set(this@FuncActivity, s.toString())

            }
        })

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(this)) {
                    screenBrightCb!!.isChecked = false
                }
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
}
