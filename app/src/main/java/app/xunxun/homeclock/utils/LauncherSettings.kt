package app.xunxun.homeclock.utils

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager

import app.xunxun.homeclock.activity.LauncherActivity

/**
 * 设置为launcher.
 * Created by fengdianxun on 2017/1/21.
 */

object LauncherSettings {

    fun setLauncher(context: Context, isCheck: Boolean) {
        val packageManager = context.packageManager
        val componentName = ComponentName(context, LauncherActivity::class.java)
        val flag = if (isCheck) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED
        packageManager.setComponentEnabledSetting(componentName, flag, PackageManager.DONT_KILL_APP)
    }
}
