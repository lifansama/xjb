package app.xunxun.homeclock;

import android.app.Activity;
import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.umeng.analytics.MobclickAgent;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengdianxun on 2017/1/24.
 */

public class MyApplication extends Application{
    private List<Activity> activities = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        MobclickAgent.setCatchUncaughtExceptions(false);
        Fabric.with(this, new Crashlytics());
    }

    public void pushActivity(Activity activity){
        activities.add(activity);
    }

    public void popActivity(Activity activity){
        activities.remove(activity);
    }

    public void clearActivities(){
        for (Activity activity: activities) {
            activity.finish();
        }
        activities.clear();
    }
}
