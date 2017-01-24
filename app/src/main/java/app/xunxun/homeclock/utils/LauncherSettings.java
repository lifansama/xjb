package app.xunxun.homeclock.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import app.xunxun.homeclock.activity.LauncherActivity;

/**
 * 设置为launcher.
 * Created by fengdianxun on 2017/1/21.
 */

public class LauncherSettings {

    public static void setLauncher(Context context, boolean isCheck) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, LauncherActivity.class);
        int flag = isCheck ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        packageManager.setComponentEnabledSetting(componentName, flag, PackageManager.DONT_KILL_APP);
    }
}
