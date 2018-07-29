package app.xunxun.homeclock.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.WindowManager

import com.umeng.analytics.MobclickAgent

import app.xunxun.homeclock.ClockViewController
import app.xunxun.homeclock.preferences.IsLauncherPreferencesDao
import app.xunxun.homeclock.utils.DoubleClickExit
import app.xunxun.homeclock.utils.LauncherSettings

/**
 * 主界面.
 */
class MainActivity : BaseActivity() {


    private var doubleClickExit: DoubleClickExit? = null

    private var clockViewController: ClockViewController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clockViewController = ClockViewController(this)
        clockViewController!!.onCreate(savedInstanceState)
        doubleClickExit = DoubleClickExit(this)
        LauncherSettings.setLauncher(this, IsLauncherPreferencesDao.get(this))
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
