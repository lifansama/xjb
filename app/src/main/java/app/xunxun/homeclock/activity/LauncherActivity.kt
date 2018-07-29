package app.xunxun.homeclock.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.MotionEvent

import com.umeng.analytics.MobclickAgent

import app.xunxun.homeclock.ClockViewController

/**
 * 启动器.
 * Created by fengdianxun on 2017/1/19.
 */

class LauncherActivity : BaseActivity() {
    internal var clockViewController: ClockViewController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clockViewController = ClockViewController(this)
        clockViewController!!.onCreate(savedInstanceState)
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

    override fun onBackPressed() {

    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, LauncherActivity::class.java))
        }
    }

}
