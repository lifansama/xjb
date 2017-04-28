package app.xunxun.homeclock.utils;

import android.app.Activity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 用于双击退出的封装方法
 */
public class DoubleClickExit {
    private static final int TIME = 2000;//间隔的时间
    private Activity activity;//传入的Activity
    boolean isExit;//判断是否退出
    private FloatToast floatToast;

    public DoubleClickExit(Activity activity) {
        this.activity = activity;
        isExit = false;
        floatToast = new FloatToast();
    }

    /**
     * 用于双击退出，单击显示Toast提示
     * 每次点击判断isExit的状态，为真则直接退出，
     * 为假则给出提示，设置isExit为真，两秒后设置isExit为假
     */
    public void doubleClickExit() {
        Timer tTask = null;
        if (!isExit) {
            isExit = true;
            if (activity != null && !activity.isFinishing()) {
                floatToast.show(activity, "再按一次退出程序", activity.getWindow().getDecorView());
            }
            tTask = new Timer();
            tTask.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, TIME);
        } else {
            activity.finish();
        }
    }
}
