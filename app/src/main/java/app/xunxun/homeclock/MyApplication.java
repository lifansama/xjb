package app.xunxun.homeclock;

import android.app.Application;

import com.pgyersdk.crash.PgyCrashManager;

/**
 * Created by fengdianxun on 2017/1/24.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        PgyCrashManager.register(this);
    }
}
