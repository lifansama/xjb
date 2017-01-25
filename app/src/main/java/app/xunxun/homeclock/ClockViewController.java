package app.xunxun.homeclock;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.SpeechError;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import app.xunxun.homeclock.activity.LauncherActivity;
import app.xunxun.homeclock.activity.MainActivity;
import app.xunxun.homeclock.activity.SettingsActivity;
import app.xunxun.homeclock.preferences.BackgroundColorPreferencesDao;
import app.xunxun.homeclock.preferences.EnableProtectScreenPreferencesDao;
import app.xunxun.homeclock.preferences.EnableSeapkWholeTimePreferencesDao;
import app.xunxun.homeclock.preferences.EnableShakeFeedbackPreferencesDao;
import app.xunxun.homeclock.preferences.Is12TimePreferencesDao;
import app.xunxun.homeclock.preferences.IsShowBatteryPreferencesDao;
import app.xunxun.homeclock.preferences.IsShowDatePreferencesDao;
import app.xunxun.homeclock.preferences.IsShowLunarPreferencesDao;
import app.xunxun.homeclock.preferences.IsShowWeekPreferencesDao;
import app.xunxun.homeclock.preferences.KeepScreenOnPreferencesDao;
import app.xunxun.homeclock.preferences.TextColorPreferencesDao;
import app.xunxun.homeclock.preferences.TextSpaceContentPreferencesDao;
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
    @InjectView(R.id.textSpaceTv)
    AutofitTextView textSpaceTv;
    private Activity activity;


    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;
    private static final SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat time24SDF = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat weekSDF = new SimpleDateFormat("E");
    private boolean navigationBarIsVisible;
    private boolean isUsefullClick;
    private BatteryChangeReceiver batteryChangeReceiver;
    private SpeechSynthesizer speechSynthesizer;
    private int backgroundColor;
    private int foregroundColor;

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
        initTypeFace();


        handler = new MyHandler();
        initListner();
        init();
        PgyCrashManager.register(activity);
        initSpeaker();

    }

    /**
     * 初始化百度语音合成.
     */
    private void initSpeaker() {
        speechSynthesizer = new SpeechSynthesizer(activity, "holer", new SpeechSynthesizerListener() {
            @Override
            public void onStartWorking(SpeechSynthesizer speechSynthesizer) {

            }

            @Override
            public void onSpeechStart(SpeechSynthesizer speechSynthesizer) {

            }

            @Override
            public void onNewDataArrive(SpeechSynthesizer speechSynthesizer, byte[] bytes, boolean b) {

            }

            @Override
            public void onBufferProgressChanged(SpeechSynthesizer speechSynthesizer, int i) {

            }

            @Override
            public void onSpeechProgressChanged(SpeechSynthesizer speechSynthesizer, int i) {

            }

            @Override
            public void onSpeechPause(SpeechSynthesizer speechSynthesizer) {

            }

            @Override
            public void onSpeechResume(SpeechSynthesizer speechSynthesizer) {

            }

            @Override
            public void onCancel(SpeechSynthesizer speechSynthesizer) {

            }

            @Override
            public void onSynthesizeFinish(SpeechSynthesizer speechSynthesizer) {

            }

            @Override
            public void onSpeechFinish(SpeechSynthesizer speechSynthesizer) {

            }

            @Override
            public void onError(SpeechSynthesizer speechSynthesizer, SpeechError speechError) {

            }
        });
        speechSynthesizer.setApiKey("oVo6NGBYqmOombGMaHuB6Eui", "ddb89bf64cc39c1a28e253e154c09af9");
        speechSynthesizer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "9");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE, SpeechSynthesizer.AUDIO_ENCODE_AMR);
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE, SpeechSynthesizer.AUDIO_BITRATE_AMR_15K85);
    }

    /**
     * 说话.
     *
     * @param txt
     */
    private void speak(final String txt) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                speechSynthesizer.speak(txt);

            }
        }).start();
    }

    /**
     * 初始化监听器.
     */
    private void initListner() {
        rootFl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.v("activityMain", "onLongClick");
                trans2Settings();
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
        setBackgroundColor();
        setForegroundColor();
        setDateTime();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryChangeReceiver = new BatteryChangeReceiver();
        activity.registerReceiver(batteryChangeReceiver, intentFilter);
        if (EnableShakeFeedbackPreferencesDao.get(activity)) {
            shakeFeedback();
        }


    }

    /**
     * 摇一摇反馈.
     */
    private void shakeFeedback() {
        PgyFeedbackShakeManager.setShakingThreshold(1000);
        PgyFeedbackShakeManager.register(activity);

    }

    public void onDestroy() {
        PgyCrashManager.unregister();

    }

    /**
     * 设置前景色.
     */
    private void setForegroundColor() {
        int color = foregroundColor;
        setForegroundColor(color);
    }

    /**
     * 设置前景色.
     *
     * @param color
     */
    private void setForegroundColor(int color) {
        timeTv.setTextColor(color);
        dateTv.setTextColor(color);
        weekTv.setTextColor(color);
        lunarTv.setTextColor(color);
        ampmTv.setTextColor(color);
        batteryTv.setTextColor(color);
        textSpaceTv.setTextColor(color);
    }

    /**
     * 设置背景色.
     */
    private void setBackgroundColor() {
        int color = backgroundColor;
        setBackgroundColor(color);
    }

    /**
     * 设置背景色.
     *
     * @param color
     */
    private void setBackgroundColor(int color) {
        activityMain.setBackgroundColor(color);
    }

    /**
     * 反转颜色.
     */
    private void toggleColor() {
        if (backgroundColor == BackgroundColorPreferencesDao.get(activity)) {
            backgroundColor = TextColorPreferencesDao.get(activity);
            foregroundColor = BackgroundColorPreferencesDao.get(activity);
        } else {
            backgroundColor = BackgroundColorPreferencesDao.get(activity);
            foregroundColor = TextColorPreferencesDao.get(activity);
        }

    }

    public void onPause() {

        timer.cancel();
        timerTask.cancel();
        timer = null;
        timerTask = null;
        activity.unregisterReceiver(batteryChangeReceiver);
        if (EnableShakeFeedbackPreferencesDao.get(activity)) {
            PgyFeedbackShakeManager.unregister();
        }
    }

    public void onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                Log.v("AA", "menu");
                trans2Settings();
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
     * 设置字体.
     */
    private void initTypeFace() {

        Typeface typeFace = Typeface.createFromAsset(activity.getAssets(), "fonts/ds_digi.ttf");
        timeTv.setTypeface(typeFace);
        dateTv.setTypeface(typeFace);

    }

    /**
     * 初始化.
     */
    private void init() {
        setTextViewVisibility(dateTv, IsShowDatePreferencesDao.get(activity));
        setTextViewVisibility(weekTv, IsShowWeekPreferencesDao.get(activity));
        setTextViewVisibility(lunarTv, IsShowLunarPreferencesDao.get(activity));
        setTextViewVisibility(batteryTv, IsShowBatteryPreferencesDao.get(activity));

        textSpaceTv.setText(TextSpaceContentPreferencesDao.get(activity));

        backgroundColor = BackgroundColorPreferencesDao.get(activity);
        foregroundColor = TextColorPreferencesDao.get(activity);

    }

    /**
     * 设置控件的显隐.
     *
     * @param textView
     * @param isShow
     */
    private void setTextViewVisibility(TextView textView, boolean isShow) {
        textView.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 更新时间.
     */
    private void setDateTime() {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        int hour24 = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int hour12 = calendar.get(Calendar.HOUR);

        setTime(now, hour24, minute, hour12);

        if (dateTv != null)
            dateTv.setText(dateSDF.format(now));
        if (weekTv != null)
            weekTv.setText(weekSDF.format(now));

        LunarCalendar lunarCalendar = LunarCalendar.getInstance(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));

        lunarTv.setText(String.format("%s月%s日", lunarCalendar.getLunarMonth(), lunarCalendar.getLunarDay()));

        if (isWholeTime(minute, second)) {
            String txt = time2SpeakContent(hour24, hour12);
            if (EnableSeapkWholeTimePreferencesDao.get(activity)) {
                speak(txt);
            }
            if (EnableProtectScreenPreferencesDao.get(activity)) {
                toggleColor();
                setBackgroundColor();
                setForegroundColor();
            }
        }
    }

    /**
     * 到整点了.
     *
     * @return
     */
    private boolean isWholeTime(int minute, int second) {
        return minute == 0 && second == 0;
    }

    /**
     * 时间转报时的话.
     *
     * @param hour24
     * @param hour12
     * @return
     */
    private String time2SpeakContent(int hour24, int hour12) {
        String result;
        if (Is12TimePreferencesDao.get(activity)) {
            if (hour12 == 0 && hour24 == 12) {
                hour12 = 12;
            }
            String ampm = hour24 >= 12 ? "下午" : "上午";
            result = String.format("%s%d点整", ampm, hour12);
        } else {
            result = String.format("%d点整", hour24);
        }
        return result;

    }

    /**
     * 设置时间.
     *
     * @param now
     * @param hour24
     * @param minute
     * @param hour12
     */
    private void setTime(Date now, int hour24, int minute, int hour12) {
        if (Is12TimePreferencesDao.get(activity)) {
            if (timeTv != null) {
                Spannable spannable = getAmPmTextSpannable(hour24, minute, hour12);

                timeTv.setText(spannable);
            }

        } else {
            if (timeTv != null) {
                timeTv.setText(time24SDF.format(now));
            }
        }
    }

    /**
     * 获取ampm模式应该显示的文字.
     *
     * @param hour24
     * @param minute
     * @param hour12
     * @return
     */
    @NonNull
    private Spannable getAmPmTextSpannable(int hour24, int minute, int hour12) {
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
        return spannable;
    }


    /**
     * 跳转到设置页面.
     */
    private void trans2Settings() {
        if (activity instanceof MainActivity) {
            SettingsActivity.start(activity, SettingsActivity.REQUEST_MAIN);
            activity.finish();

        } else if (activity instanceof LauncherActivity) {
            SettingsActivity.start(activity, SettingsActivity.REQUEST_LAUNCHER);
            activity.finish();

        }
    }

    /**
     * 电量监听器.
     */
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

    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                setDateTime();
            }
        }
    }

}
