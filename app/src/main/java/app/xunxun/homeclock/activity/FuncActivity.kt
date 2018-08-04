package app.xunxun.homeclock.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.view.View
import app.xunxun.homeclock.MyService
import app.xunxun.homeclock.R
import app.xunxun.homeclock.pref.SimplePref
import app.xunxun.homeclock.utils.LauncherSettings
import kotlinx.android.synthetic.main.activity_func.*
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity

class FuncActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_func)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        protectScreenCb!!.isChecked = SimplePref.create(this).enableProtectScreen().get()



        keepScreenOnCb!!.setOnCheckedChangeListener { compoundButton, isCheck -> SimplePref.create(this).keepScreenOn().set(isCheck) }
        keepScreenOnCb!!.isChecked = SimplePref.create(this).keepScreenOn().get()



        setLauncherCb!!.isChecked = SimplePref.create(this).isLauncher().get()
        setLauncherCb!!.setOnCheckedChangeListener { compoundButton, isCheck ->
            SimplePref.create(this).isLauncher().set(isCheck)


            LauncherSettings.setLauncher(compoundButton.context, isCheck)

            if (isCheck) {
                val selector = Intent(Intent.ACTION_MAIN)
                selector.addCategory(Intent.CATEGORY_HOME)
                compoundButton.context.startActivity(selector)
            }
        }
        enableSpeakWholeTimeCb.onCheckedChange { buttonView, isChecked ->
            SimplePref.create(buttonView!!.context).enableVibrateWholeTime().set(isChecked)
        }
        enableVoiceWholeTimeCb.onCheckedChange { buttonView, isChecked ->
            SimplePref.create(buttonView!!.context).enableVoiceWholeTime().set(isChecked)
        }
        protectScreenCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            SimplePref.create(buttonView.context).enableProtectScreen().set(isChecked)
            if (isChecked)
                showAlert("开启防烧屏后文字会5分钟换一次位置，如果字太大影响移动后的显示请自行调小。")
        }
        lockScreenShowCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            SimplePref.create(buttonView.context).lockScreenShowOn().set(isChecked)
            if (isChecked) {
                MyService.startService(this@FuncActivity)
            } else {
                MyService.stopService(this@FuncActivity)
            }
        }
        alertTv.onClick { startActivity<AlertActivity>() }
        
        if (SimplePref.create(this).screenOrientation().get() == ActivityInfo.SCREEN_ORIENTATION_SENSOR) {
            sensorRb!!.isChecked = true
        } else if (SimplePref.create(this).screenOrientation().get() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            landscapeRb!!.isChecked = true
        } else if (SimplePref.create(this).screenOrientation().get() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            portraitRb!!.isChecked = true
        }
        screenOrientationRg!!.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.sensorRb) {
                SimplePref.create(this).screenOrientation().set(ActivityInfo.SCREEN_ORIENTATION_SENSOR)

            } else if (checkedId == R.id.landscapeRb) {
                SimplePref.create(this).screenOrientation().set(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

            } else if (checkedId == R.id.portraitRb) {
                SimplePref.create(this).screenOrientation().set(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

            }
        }
        screenBrightCb!!.visibility = View.GONE
        notifyStayCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            SimplePref.create(buttonView!!.context).notifyStay().set(isChecked)
            if (isChecked) {
                MyService.startService(buttonView.context)
            } else {
                MyService.stopService(buttonView.context)

            }
        }
        enableSpeakWholeTimeCb!!.isChecked = SimplePref.create(this).enableVibrateWholeTime().get()
        enableVoiceWholeTimeCb!!.isChecked = SimplePref.create(this).enableVoiceWholeTime().get()
        lockScreenShowCb!!.isChecked = SimplePref.create(this).lockScreenShowOn().get()
        notifyStayCb!!.isChecked = SimplePref.create(this).notifyStay().get()
        LauncherSettings.setLauncher(this, SimplePref.create(this).isLauncher().get())


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
