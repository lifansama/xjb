package app.xunxun.homeclock

import android.app.Activity
import android.app.Application

//import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.Crashlytics
import com.umeng.analytics.MobclickAgent

//import io.fabric.sdk.android.Fabric;
import java.util.ArrayList

import io.fabric.sdk.android.Fabric

/**
 * Created by fengdianxun on 2017/1/24.
 */

class MyApplication : Application() {
    private val activities = ArrayList<Activity>()
    override fun onCreate() {
        super.onCreate()
        MobclickAgent.setCatchUncaughtExceptions(false)
        Fabric.with(this, Crashlytics())
    }

    fun pushActivity(activity: Activity) {
        activities.add(activity)
    }

    fun popActivity(activity: Activity) {
        activities.remove(activity)
    }

    fun clearActivities() {
        for (activity in activities) {
            activity.finish()
        }
        activities.clear()
    }
}
