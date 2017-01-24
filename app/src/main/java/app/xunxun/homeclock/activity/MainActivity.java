package app.xunxun.homeclock.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.umeng.analytics.MobclickAgent;

import app.xunxun.homeclock.ClockViewController;
import app.xunxun.homeclock.preferences.IsLauncherPreferencesDao;
import app.xunxun.homeclock.utils.DoubleClickExit;
import app.xunxun.homeclock.utils.LauncherSettings;

/**
 * 主界面.
 */
public class MainActivity extends AppCompatActivity {


    private DoubleClickExit doubleClickExit;

    private ClockViewController clockViewController;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clockViewController = new ClockViewController(this);
        clockViewController.onCreate(savedInstanceState);
        doubleClickExit = new DoubleClickExit(this);
        LauncherSettings.setLauncher(this, IsLauncherPreferencesDao.get(this));
    }



    @Override
    public void onBackPressed() {
        doubleClickExit.doubleClickExit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        clockViewController.onResume();
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        clockViewController.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        clockViewController.onKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clockViewController = null;
    }
}
