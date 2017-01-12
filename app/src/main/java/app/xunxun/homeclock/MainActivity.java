package app.xunxun.homeclock;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import app.xunxun.homeclock.activity.SettingsActivity;
import app.xunxun.homeclock.preferences.BackgroundColorPreferencesDao;
import app.xunxun.homeclock.preferences.TextColorPreferencesDao;
import app.xunxun.homeclock.utils.DoubleClickExit;
import butterknife.ButterKnife;
import butterknife.InjectView;

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

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;
    private SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat weekSDF = new SimpleDateFormat("E");

    private DoubleClickExit doubleClickExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    Calendar calendar = Calendar.getInstance();
                    if (hoursTv != null && minusTv != null && secondTv != null){
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        int second = calendar.get(Calendar.SECOND);
                        hoursTv.setText(String.format("%02d",hours));
                        minusTv.setText(String.format("%02d",minute));
                        secondTv.setText(String.format("%02d",second));
                    }
                    Date now = new Date();
                    if (dateTv != null)
                        dateTv.setText(dateSDF.format(now));
                    if (weekTv != null)
                        weekTv.setText(weekSDF.format(now));
                }
            }
        };
        doubleClickExit = new DoubleClickExit(this);
        rootFl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.v("activityMain", "onLongClick");
                SettingsActivity.start(MainActivity.this);
                return false;
            }
        });

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
        dateTv.setTextColor(TextColorPreferencesDao.get(this));
        weekTv.setTextColor(TextColorPreferencesDao.get(this));
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

}
