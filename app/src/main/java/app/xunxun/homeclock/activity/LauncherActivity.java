package app.xunxun.homeclock.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.umeng.analytics.MobclickAgent;

import app.xunxun.homeclock.ClockViewController;

/**
 * 启动器.
 * Created by fengdianxun on 2017/1/19.
 */

public class LauncherActivity extends AppCompatActivity {
    ClockViewController clockViewController;

    public static void start(Context context) {
        context.startActivity(new Intent(context, LauncherActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clockViewController = new ClockViewController(this);
        clockViewController.onCreate(savedInstanceState);
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
        clockViewController.onDestroy();
        clockViewController = null;
    }

    @Override
    public void onBackPressed() {

    }
}
