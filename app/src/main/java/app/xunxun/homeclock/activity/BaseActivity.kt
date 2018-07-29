package app.xunxun.homeclock.activity

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager

import app.xunxun.homeclock.MyApplication
import app.xunxun.homeclock.preferences.KeepScreenOnPreferencesDao

/**
 * Created by fengdianxun on 2017/4/22.
 */

open class BaseActivity : AppCompatActivity() {

    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        if (KeepScreenOnPreferencesDao.get(this)) {
            Log.v("onCreate", "FLAG_KEEP_SCREEN_ON")
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        }
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        val app = application as MyApplication
        app.pushActivity(this)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
        val app = application as MyApplication
        app.popActivity(this)
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        //        if (requestCode == 100){
        //            if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_DENIED) {
        //                showAlert("由于您拒绝了授权，检查更新功能将不能正常使用!");
        //            }
        //        }
    }

    private fun showAlert(msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("温馨提醒")
        builder.setMessage(msg)
        builder.setPositiveButton("知道了", null)
        dialog = builder.show()

    }
}
