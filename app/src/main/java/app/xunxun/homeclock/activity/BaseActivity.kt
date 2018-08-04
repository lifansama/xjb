package app.xunxun.homeclock.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import app.xunxun.homeclock.MyApplication
import app.xunxun.homeclock.R
import app.xunxun.homeclock.pref.SimplePref
import com.umeng.analytics.MobclickAgent
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.intentFor

/**
 * Created by fengdianxun on 2017/4/22.
 */

open class BaseActivity : AppCompatActivity() {

    private var dialog: AlertDialog? = null
    private var countDownTimer: CountDownTimer? = null
    protected var disableCountDown: Boolean = false
    private lateinit var initialTitle: CharSequence

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialTitle = title

        if (SimplePref.create(this).keepScreenOn().get()) {
            Log.v("onCreate", "FLAG_KEEP_SCREEN_ON")
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        }
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        val app = application as MyApplication
        app.pushActivity(this)
        if (!disableCountDown) {
            countDownTimer = MyCountDown((60 * 1000).toLong(), 1000)
        }

    }

    override fun onDestroy() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
        val app = application as MyApplication
        app.popActivity(this)
        super.onDestroy()
    }


    public override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
        countDownTimer?.start()
    }

    public override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
        countDownTimer?.cancel()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        //        if (requestCode == 100){
        //            if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_DENIED) {
        //                showAlert("由于您拒绝了授权，检查更新功能将不能正常使用!");
        //            }
        //        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        } else if (item.itemId == R.id.action_home) {
            startActivity(intentFor<MainActivity>().clearTop())
        }
        return super.onOptionsItemSelected(item)
    }


    internal inner class MyCountDown
    (millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            title = String.format("$initialTitle(%s秒)", millisUntilFinished / 1000)

        }

        override fun onFinish() {
            onBackPressed()

        }
    }
}
