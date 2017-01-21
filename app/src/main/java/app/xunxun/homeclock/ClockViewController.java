package app.xunxun.homeclock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import app.xunxun.homeclock.activity.LauncherActivity;
import app.xunxun.homeclock.activity.MainActivity;
import app.xunxun.homeclock.activity.SettingsActivity;
import app.xunxun.homeclock.preferences.BackgroundColorPreferencesDao;
import app.xunxun.homeclock.preferences.Is12TimePreferencesDao;
import app.xunxun.homeclock.preferences.IsShowBatteryPreferencesDao;
import app.xunxun.homeclock.preferences.IsShowDatePreferencesDao;
import app.xunxun.homeclock.preferences.IsShowLunarPreferencesDao;
import app.xunxun.homeclock.preferences.IsShowWeekPreferencesDao;
import app.xunxun.homeclock.preferences.KeepScreenOnPreferencesDao;
import app.xunxun.homeclock.preferences.TextColorPreferencesDao;
import butterknife.ButterKnife;
import butterknife.InjectView;
import io.github.xhinliang.lunarcalendar.LunarCalendar;
import me.grantland.widget.AutofitTextView;

/**
 * Created by fengdianxun on 2017/1/19.
 */

public class ClockViewController {

    public static final int WHAT_TIME = 1;

    @InjectView(R.id.dateTv)
    TextView dateTv;
    @InjectView(R.id.weekTv)
    TextView weekTv;
    @InjectView(R.id.lunarTv)
    TextView lunarTv;
    @InjectView(R.id.dateLl)
    LinearLayout dateLl;
    @InjectView(R.id.timeTv)
    AutofitTextView timeTv;
    @InjectView(R.id.ampmTv)
    TextView ampmTv;
    @InjectView(R.id.activity_main)
    RelativeLayout activityMain;
    @InjectView(R.id.rootFl)
    FrameLayout rootFl;
    @InjectView(R.id.batteryTv)
    TextView batteryTv;
    private Activity activity;


    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;
    private SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat time24SDF = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat weekSDF = new SimpleDateFormat("E");
    private boolean navigationBarIsVisible;
    private boolean isUsefullClick;
    private BatteryChangeReceiver batteryChangeReceiver;

    public ClockViewController(Activity activity) {
        this.activity = activity;
    }

    public void onCreate(Bundle savedInstanceState) {
        hideNavigationBar();
        activity.setContentView(R.layout.activity_main);
        if (KeepScreenOnPreferencesDao.get(activity)) {
            Log.v("onCreate", "FLAG_KEEP_SCREEN_ON");
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }
        ButterKnife.inject(this, activity);

        Typeface typeFace = Typeface.createFromAsset(activity.getAssets(), "fonts/ds_digi.ttf");
        timeTv.setTypeface(typeFace);
        dateTv.setTypeface(typeFace);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    setTime();
                }
            }
        };
        rootFl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.v("activityMain", "onLongClick");
                trans2Settins();
                return false;
            }
        });
        rootFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.requestFocus();
                if (isUsefullClick && navigationBarIsVisible) {
                    Log.v("activityMain", "hideNavigationBar");
                    hideNavigationBar();
                    isUsefullClick = false;
                }
            }
        });
        if (Build.VERSION.SDK_INT >= 11) {
            activity.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
                        // TODO: The navigation bar is visible. Make any desired
                        // adjustments to your UI, such as showing the action bar or
                        // other navigational controls.
                        Log.v("AA", "The navigation bar is visible");
                        navigationBarIsVisible = true;
                        isUsefullClick = true;
                    } else {
                        // TODO: The navigation bar is NOT visible. Make any desired
                        // adjustments to your UI, such as hiding the action bar or
                        // other navigational controls.
                        Log.v("AA", "The navigation bar is NOT visible");
                        navigationBarIsVisible = false;
                        isUsefullClick = true;

                    }
                }
            });
        }
        dateTv.setVisibility(IsShowDatePreferencesDao.get(activity) ? View.VISIBLE : View.GONE);
        weekTv.setVisibility(IsShowWeekPreferencesDao.get(activity) ? View.VISIBLE : View.GONE);
        lunarTv.setVisibility(IsShowLunarPreferencesDao.get(activity) ? View.VISIBLE : View.GONE);
        batteryTv.setVisibility(IsShowBatteryPreferencesDao.get(activity) ? View.VISIBLE : View.GONE);
    }

    public void onResume() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(WHAT_TIME);
            }
        };
        timer.schedule(timerTask, 1000, 1000);
        activityMain.setBackgroundColor(BackgroundColorPreferencesDao.get(activity));
        timeTv.setTextColor(TextColorPreferencesDao.get(activity));
        dateTv.setTextColor(TextColorPreferencesDao.get(activity));
        weekTv.setTextColor(TextColorPreferencesDao.get(activity));
        lunarTv.setTextColor(TextColorPreferencesDao.get(activity));
        ampmTv.setTextColor(TextColorPreferencesDao.get(activity));
        batteryTv.setTextColor(TextColorPreferencesDao.get(activity));
        setTime();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryChangeReceiver = new BatteryChangeReceiver();
        activity.registerReceiver(batteryChangeReceiver, intentFilter);

    }

    public void onPause() {
        timer.cancel();
        timerTask.cancel();
        timer = null;
        timerTask = null;
        activity.unregisterReceiver(batteryChangeReceiver);
    }

    public void onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                Log.v("AA", "menu");
                trans2Settins();
                break;
        }
    }

    /**
     * Detects and toggles immersive mode (also known as "hidey bar" mode).
     */
    public void hideNavigationBar() {

        View decorView = activity.getWindow().getDecorView();
        decorView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= 11) {
                    activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                }

            }
        }, 800);
    }

    /**
     * 更新时间.
     */
    private void setTime() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        int hour24 = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int hour12 = calendar.get(Calendar.HOUR);

        if (Is12TimePreferencesDao.get(activity)) {
            if (timeTv != null) {
                if (hour12 == 0 && hour24 == 12) {
                    hour12 = 12;
                }
                String ampm = hour24 >= 12 ? "PM" : "AM";
                String time = String.format("%02d:%02d%s", hour12, minute, ampm);
                Spannable spannable = new SpannableString(time);
                int start = 5;
                int end = 7;
                spannable.setSpan(new RelativeSizeSpan(0.6f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new TypefaceSpan("default"), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                timeTv.setText(spannable);
            }

        } else {
            if (timeTv != null) {
                timeTv.setText(time24SDF.format(now));
            }
        }
//        }
        if (dateTv != null)
            dateTv.setText(dateSDF.format(now));
        if (weekTv != null)
            weekTv.setText(weekSDF.format(now));

        LunarCalendar lunarCalendar = LunarCalendar.getInstance(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));

        lunarTv.setText(String.format("%s月%s日", lunarCalendar.getLunarMonth(), lunarCalendar.getLunarDay()));
    }


    private void trans2Settins() {
        if (activity instanceof MainActivity) {
            SettingsActivity.start(activity, SettingsActivity.REQUEST_MAIN);
            activity.finish();

        } else if (activity instanceof LauncherActivity) {
            SettingsActivity.start(activity, SettingsActivity.REQUEST_LAUNCHER);
            activity.finish();

        }
    }

    public class BatteryChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                //获取当前电量
                int level = intent.getIntExtra("level", 0);
                //电量的总刻度
                int scale = intent.getIntExtra("scale", 100);
                //把它转成百分比
                if (batteryTv != null) {
                    batteryTv.setText(String.format("电量:%d%%", (level * 100) / scale));
                }
            }
        }
    }

}
