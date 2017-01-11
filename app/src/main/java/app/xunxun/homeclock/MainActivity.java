package app.xunxun.homeclock;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
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
    @InjectView(R.id.timeTv)
    TextView timeTv;
    @InjectView(R.id.dateTv)
    TextView dateTv;
    @InjectView(R.id.weekTv)
    TextView weekTv;
    @InjectView(R.id.activity_main)
    RelativeLayout activityMain;

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;
    private SimpleDateFormat timeSDF = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat weekSDF = new SimpleDateFormat("E");

    private DoubleClickExit doubleClickExit;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            window.setAttributes(params);
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
//                if (msg.what == 1) {
//                    Date now = new Date();
//                    if (timeTv != null)
//                        timeTv.setText(timeSDF.format(now));
//                    if (dateTv != null)
//                        dateTv.setText(dateSDF.format(now));
//                    if (weekTv != null)
//                        weekTv.setText(weekSDF.format(now));
//                }
            }
        };
        doubleClickExit = new DoubleClickExit(this);
        gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                Log.v("GestureDetector", "onDown");
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {
                Log.v("GestureDetector", "onShowPress");

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                Log.v("GestureDetector", "onSingleTapUp");
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                Log.v("GestureDetector", "onScroll");
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {
                Log.v("GestureDetector", "onLongPress");

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                Log.v("GestureDetector", "onFling");
                SettingsActivity.start(MainActivity.this);
                return false;
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v("MainActivity", "onTouchEvent");
        return gestureDetector.onTouchEvent(event);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        timerTask.cancel();
        timer = null;
        timerTask = null;
    }

}
