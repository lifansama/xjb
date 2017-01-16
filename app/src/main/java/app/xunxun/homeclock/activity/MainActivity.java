package app.xunxun.homeclock.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
    @InjectView(R.id.hoursTv)
    TextView hoursTv;
    @InjectView(R.id.minusTv)
    TextView minusTv;
    @InjectView(R.id.secondTv)
    TextView secondTv;
    @InjectView(R.id.placeholderHoursTv)
    TextView placeholderHoursTv;
    @InjectView(R.id.placeholderMinusTv)
    TextView placeholderMinusTv;
    @InjectView(R.id.placeholderSecondTv)
    TextView placeholderSecondTv;
    @InjectView(R.id.maohao1Tv)
    TextView maohao1Tv;
    @InjectView(R.id.maohao2Tv)
    TextView maohao2Tv;
    @InjectView(R.id.lunarTv)
    TextView lunarTv;
    @InjectView(R.id.ampmTv)
    TextView ampmTv;
    @InjectView(R.id.dateLl)
    LinearLayout dateLl;
    @InjectView(R.id.secondFl)
    FrameLayout secondFl;

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;
    private SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy-MM-dd");
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
        hoursTv.setTypeface(typeFace);
        minusTv.setTypeface(typeFace);
        secondTv.setTypeface(typeFace);
        placeholderHoursTv.setTypeface(typeFace);
        placeholderMinusTv.setTypeface(typeFace);
        placeholderSecondTv.setTypeface(typeFace);
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
                SettingsActivity.start(MainActivity.this);
                finish();
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

    /**
     * 更新时间.
     */
    private void setTime() {
        Calendar calendar = Calendar.getInstance();
        if (hoursTv != null && minusTv != null && secondTv != null) {
            int hour24 = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            int hour12 = calendar.get(Calendar.HOUR);

            if (Is12TimePreferencesDao.get(MainActivity.this)) {
                hoursTv.setText(String.format("%02d", hour12));
                minusTv.setText(String.format("%02d", minute));
                secondFl.setVisibility(View.GONE);
                maohao2Tv.setVisibility(View.GONE);
                ampmTv.setVisibility(View.VISIBLE);
                ampmTv.setText(hour24 >= 12 ? "PM" : "AM");

            } else {
                hoursTv.setText(String.format("%02d", hour24));
                minusTv.setText(String.format("%02d", minute));
                secondTv.setText(String.format("%02d", second));
                secondFl.setVisibility(View.VISIBLE);
                maohao2Tv.setVisibility(View.VISIBLE);
                ampmTv.setVisibility(View.GONE);
            }
        }
        Date now = new Date();
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
        hoursTv.setTextColor(TextColorPreferencesDao.get(this));
        minusTv.setTextColor(TextColorPreferencesDao.get(this));
        secondTv.setTextColor(TextColorPreferencesDao.get(this));
        maohao1Tv.setTextColor(TextColorPreferencesDao.get(this));
        maohao2Tv.setTextColor(TextColorPreferencesDao.get(this));
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
}
