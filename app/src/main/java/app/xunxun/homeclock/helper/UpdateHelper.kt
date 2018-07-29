package app.xunxun.homeclock.helper

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog

import com.pgyersdk.javabean.AppBean
import com.pgyersdk.update.PgyUpdateManager
import com.pgyersdk.update.UpdateManagerListener

import app.xunxun.homeclock.utils.FloatToast

/**
 * Created by fengdianxun on 2017/5/2.
 */

class UpdateHelper(private val mActivity: Activity?) {

    fun check(force: Boolean) {

        val flag = ContextCompat.checkSelfPermission(mActivity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (PackageManager.PERMISSION_GRANTED == flag) {
            PgyUpdateManager.register(mActivity, "app.xunxun.homeclock",
                    object : UpdateManagerListener() {

                        override fun onUpdateAvailable(result: String) {

                            // 将新版本信息封装到AppBean中
                            if (mActivity != null && !mActivity.isFinishing) {
                                val appBean = UpdateManagerListener.getAppBeanFromString(result)
                                AlertDialog.Builder(mActivity)
                                        .setTitle("更新")
                                        .setMessage(appBean.releaseNote)
                                        .setPositiveButton(
                                                "确定"
                                        ) { dialog, which ->
                                            UpdateManagerListener.startDownloadTask(
                                                    mActivity,
                                                    appBean.downloadURL)
                                        }
                                        .setNegativeButton("取消", null)
                                        .show()
                            }
                            PgyUpdateManager.unregister()

                        }

                        override fun onNoUpdateAvailable() {
                            if (force) {
                                val floatToast = FloatToast()
                                floatToast.show(mActivity, "已是最新版", mActivity.window.decorView)
                            }
                            PgyUpdateManager.unregister()
                        }
                    })
        } else {
            ActivityCompat.requestPermissions(mActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        }


    }
}
