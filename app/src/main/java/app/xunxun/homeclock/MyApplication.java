package app.xunxun.homeclock;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.pgyersdk.crash.PgyCrashManager;

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
        PgyCrashManager.register(this);
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
