package app.xunxun.homeclock.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent

import com.umeng.analytics.MobclickAgent

import app.xunxun.homeclock.ClockViewController
import app.xunxun.homeclock.pref.SimplePref
import app.xunxun.homeclock.utils.DoubleClickExit
import app.xunxun.homeclock.utils.LauncherSettings
import org.jetbrains.anko.toast

/**
 * 主界面.
 */
class MainActivity : BaseActivity() {


    private var doubleClickExit: DoubleClickExit? = null

    private var clockViewController: ClockViewController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        disableCountDown = true
        super.onCreate(savedInstanceState)
       val backgroundColorSpan =  SimplePref.create(this).backgroundColor().get()
        toast(backgroundColorSpan.toString())

        clockViewController = ClockViewController(this)
        clockViewController!!.onCreate(savedInstanceState)
        doubleClickExit = DoubleClickExit(this)
        LauncherSettings.setLauncher(this, SimplePref.create(this).isLauncher().get())
    }

    override fun onBackPressed() {
        doubleClickExit!!.doubleClickExit()
    }

    override fun onResume() {
        super.onResume()
        clockViewController!!.onResume()
        MobclickAgent.onResume(this)

    }

    override fun onPause() {
        super.onPause()
        clockViewController!!.onPause()
        MobclickAgent.onPause(this)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        clockViewController!!.onKeyDown(keyCode, event)
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        clockViewController!!.onDestroy()
        clockViewController = null
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

}
