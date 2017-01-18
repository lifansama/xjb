package app.xunxun.homeclock.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import app.xunxun.homeclock.R;
import app.xunxun.homeclock.preferences.BackgroundColorPreferencesDao;
import app.xunxun.homeclock.preferences.Is12TimePreferencesDao;
import app.xunxun.homeclock.preferences.KeepScreenOnPreferencesDao;
import app.xunxun.homeclock.preferences.TextColorPreferencesDao;
import app.xunxun.homeclock.utils.DoubleClickExit;
import butterknife.ButterKnife;
import butterknife.InjectView;
import io.github.xhinliang.lunarcalendar.LunarCalendar;


public class MainActivity extends AppCompatActivity {

    public static final int WHAT_TIME = 1;
    @InjectView(R.id.dateTv)
    TextView dateTv;
    @InjectView(R.id.weekTv)
    TextView weekTv;
    @InjectView(R.id.activity_main)
    RelativeLayout activityMain;
    @InjectView(R.id.rootFl)
    FrameLayout rootFl;
    @InjectView(R.id.lunarTv)
    TextView lunarTv;
    @InjectView(R.id.ampmTv)
    TextView ampmTv;
    @InjectView(R.id.dateLl)
    LinearLayout dateLl;
    @InjectView(R.id.timeTv)
    TextView timeTv;

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;
    private SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat time24SDF = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat weekSDF = new SimpleDateFormat("E");

    private DoubleClickExit doubleClickExit;
    private boolean navigationBarIsVisible;
    private boolean isUsefullClick;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideNavigationBar();
        if (KeepScreenOnPreferencesDao.get(this)) {
            Log.v("onCreate", "FLAG_KEEP_SCREEN_ON");
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/ds_digi.ttf");
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
        doubleClickExit = new DoubleClickExit(this);
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
            getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
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

    }

    private void trans2Settins() {
        SettingsActivity.start(MainActivity.this);
        finish();
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

        if (Is12TimePreferencesDao.get(MainActivity.this)) {
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


    @Override
    public void onBackPressed() {
        doubleClickExit.doubleClickExit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(WHAT_TIME);
            }
        };
        timer.schedule(timerTask, 1000, 1000);
        activityMain.setBackgroundColor(BackgroundColorPreferencesDao.get(this));
        timeTv.setTextColor(TextColorPreferencesDao.get(this));
        dateTv.setTextColor(TextColorPreferencesDao.get(this));
        weekTv.setTextColor(TextColorPreferencesDao.get(this));
        lunarTv.setTextColor(TextColorPreferencesDao.get(this));
        ampmTv.setTextColor(TextColorPreferencesDao.get(this));
        setTime();
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        timerTask.cancel();
        timer = null;
        timerTask = null;
        MobclickAgent.onPause(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v("BB", "onTouchEvent");
        return super.onTouchEvent(event);
    }

    /**
     * Detects and toggles immersive mode (also known as "hidey bar" mode).
     */
    public void hideNavigationBar() {

        View decorView = getWindow().getDecorView();
        decorView.postDelayed(new Runnable() {
            @Override
            public void run() {
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= 11) {
                    getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                }

            }
        }, 800);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_MENU:
                Log.v("AA","menu");
                trans2Settins();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
