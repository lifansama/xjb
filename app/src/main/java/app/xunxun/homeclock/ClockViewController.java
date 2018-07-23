package app.xunxun.homeclock;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.crashlytics.android.Crashlytics;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import app.xunxun.homeclock.activity.LauncherActivity;
import app.xunxun.homeclock.activity.MainActivity;
import app.xunxun.homeclock.activity.SettingsActivity;
import app.xunxun.homeclock.api.Api;
import app.xunxun.homeclock.helper.SoundPoolHelper;
import app.xunxun.homeclock.helper.UpdateHelper;
import app.xunxun.homeclock.model.Pic;
import app.xunxun.homeclock.preferences.BackgroundColorPreferencesDao;
import app.xunxun.homeclock.preferences.BackgroundModePreferencesDao;
import app.xunxun.homeclock.preferences.EnableProtectScreenPreferencesDao;
import app.xunxun.homeclock.preferences.EnableVibrateWholeTimePreferencesDao;
import app.xunxun.homeclock.preferences.EnableShakeFeedbackPreferencesDao;
import app.xunxun.homeclock.preferences.EnableVoiceWholeTimePreferencesDao;
import app.xunxun.homeclock.preferences.FocusTimePreferencesDao;
import app.xunxun.homeclock.preferences.Is12TimePreferencesDao;
import app.xunxun.homeclock.preferences.IsMaoHaoShanShuoPreferencesDao;
import app.xunxun.homeclock.preferences.IsShowBatteryPreferencesDao;
import app.xunxun.homeclock.preferences.IsShowDatePreferencesDao;
import app.xunxun.homeclock.preferences.IsShowLunarPreferencesDao;
import app.xunxun.homeclock.preferences.IsShowWeekPreferencesDao;
import app.xunxun.homeclock.preferences.LocalImageFilePathPreferencesDao;
import app.xunxun.homeclock.preferences.LockScreenShowOnPreferencesDao;
import app.xunxun.homeclock.preferences.ScreenBrightnessPreferencesDao;
import app.xunxun.homeclock.preferences.ScreenOrientationPreferencesDao;
import app.xunxun.homeclock.preferences.ShowSecondPreferencesDao;
import app.xunxun.homeclock.preferences.TextColorPreferencesDao;
import app.xunxun.homeclock.preferences.TextSizePreferencesDao;
import app.xunxun.homeclock.preferences.TextSpaceContentPreferencesDao;
import app.xunxun.homeclock.utils.FloatToast;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.xhinliang.lunarcalendar.LunarCalendar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fengdianxun on 2017/1/19.
 */

public class ClockViewController {

    public static final int WHAT_TIME = 1;

    @BindView(R.id.dateTv)
    TextView dateTv;
    @BindView(R.id.weekTv)
    TextView weekTv;
    @BindView(R.id.lunarTv)
    TextView lunarTv;
    @BindView(R.id.dateLl)
    LinearLayout dateLl;
    @BindView(R.id.timeTv)
    TextView timeTv;
    @BindView(R.id.ampmTv)
    TextView ampmTv;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    @BindView(R.id.rootFl)
    FrameLayout rootFl;
    @BindView(R.id.batteryTv)
    TextView batteryTv;
    @BindView(R.id.textSpaceTv)
    TextView textSpaceTv;
    @BindView(R.id.timeLl)
    LinearLayout timeLl;
    @BindView(R.id.centerRl)
    RelativeLayout centerRl;
    @BindView(R.id.battery2Tv)
    TextView battery2Tv;
    @BindView(R.id.backIv)
    ImageView backIv;
    @BindView(R.id.focusTimeTv)
    TextView focusTimeTv;
    private Activity activity;


    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;
    private static final SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat time24SDF = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat time24NoSecondSDF = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat time24NoSecondNOMaoHaoSDF = new SimpleDateFormat("HH mm");
    private static final SimpleDateFormat weekSDF = new SimpleDateFormat("E");
    private boolean navigationBarIsVisible;
    private boolean isUsefullClick;
    private BatteryChangeReceiver batteryChangeReceiver;
    private int backgroundColor;
    private int foregroundColor;
    private Vibrator vibrator;
    private long lastTime;
    private int screenWidth;
    private int screenHeight;
    private int currentLight; // 当前屏幕的亮度
    private UpdateHelper updateHelper;
    private GestureDetector gestureDetector;
    private FloatToast toast;
    private SoundPoolHelper soundPoolHelper;

    public ClockViewController(Activity activity) {
        this.activity = activity;
    }

    public void onCreate(Bundle savedInstanceState) {
        if (ScreenOrientationPreferencesDao.get(activity) == ActivityInfo.SCREEN_ORIENTATION_SENSOR)
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        else if (ScreenOrientationPreferencesDao.get(activity) == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else if (ScreenOrientationPreferencesDao.get(activity) == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        hideNavigationBar();
        activity.setContentView(R.layout.activity_main);
        ButterKnife.bind(this, activity);
        initTypeFace();


        handler = new MyHandler();
        initListner();
        init();
        vibrator = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        if (LockScreenShowOnPreferencesDao.get(activity)) {
            MyService.startService(activity);
        }
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        updateHelper = new UpdateHelper(activity);
        updateHelper.check(false);
        gestureDetector = new GestureDetector(activity, new MyGestureListener());
        int flag = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_SETTINGS);
        if (flag != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_SETTINGS}, 101);
        }

        toast = new FloatToast();

        soundPoolHelper = new SoundPoolHelper(activity);
        soundPoolHelper.load();
    }


    /**
     * 说话.
     */
    private void speak(String filename) {

        if (EnableVibrateWholeTimePreferencesDao.get(activity)) {
            vibrator.vibrate(1000);
        }
        if (EnableVoiceWholeTimePreferencesDao.get(activity)) {
            soundPoolHelper.play(filename);
        }
    }

    /**
     * 初始化监听器.
     */
    private void initListner() {
        rootFl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
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

        try {
            int screenMode = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
            int screenBrightness = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            ScreenBrightnessPreferencesDao.setSysMode(activity, screenMode);
            ScreenBrightnessPreferencesDao.setSysValue(activity, screenBrightness);
            currentLight = screenBrightness;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void onPause() {
        if (timer != null) {
            timer.cancel();
            timer = null;

        }

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        activity.unregisterReceiver(batteryChangeReceiver);
        if (EnableShakeFeedbackPreferencesDao.get(activity)) {
            PgyFeedbackShakeManager.unregister();
        }

        if (isHaveWriteSettinsPermisson()) {
            setScreenMode(ScreenBrightnessPreferencesDao.getSysMode(activity));
            setScreenBrightness(ScreenBrightnessPreferencesDao.getSysValue(activity));
        }
    }

    private boolean isHaveWriteSettinsPermisson() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.System.canWrite(activity);
        } else {
            return true;
        }

    }

    /**
     * 设置当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    private void setScreenMode(int value) {
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, value);
    }

    /**
     * 设置当前屏幕亮度值 0--255，并使之生效
     */
    private void setScreenBrightness(float value) {
        Window mWindow = activity.getWindow();
        WindowManager.LayoutParams mParams = mWindow.getAttributes();
        float f = value / 255.0F;
        mParams.screenBrightness = f;
        mWindow.setAttributes(mParams);

        // 保存设置的屏幕亮度值
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) value);
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
        soundPoolHelper.release();

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
        battery2Tv.setTextColor(color);
        textSpaceTv.setTextColor(color);
        focusTimeTv.setTextColor(color);
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
        int mode = BackgroundModePreferencesDao.get(activity);

        if (mode == BackgroundModePreferencesDao.MODE_ONLINE_IMAGE) {
            getPic();
        } else if (mode == BackgroundModePreferencesDao.MODE_COLOR) {
            activityMain.setBackgroundColor(color);
        } else if (mode == BackgroundModePreferencesDao.MODE_LOCAL_IMAGE) {
            activityMain.setBackgroundColor(Color.argb(100, 0, 0, 0));
            String path = LocalImageFilePathPreferencesDao.get(activity);
            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                Picasso.with(activity).load(file).into(backIv);
            } else {
                activityMain.setBackgroundColor(color);
            }
        }

    }

    private void getPic() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.bing.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        api.getPic().enqueue(new Callback<Pic>() {
            @Override
            public void onResponse(Call<Pic> call, Response<Pic> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getImages().isEmpty()) {

                    Pic.ImagesEntity imagesEntity = response.body().getImages().get(0);
                    String url = imagesEntity.getUrl();

                    activityMain.setBackgroundColor(Color.argb(100, 0, 0, 0));
                    Picasso.with(activity).load(String.format("%s%s", "http://www.bing.com/", url)).into(backIv);

                }
            }

            @Override
            public void onFailure(Call<Pic> call, Throwable t) {

            }
        });
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

        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            timeTv.setTextSize(TextSizePreferencesDao.get(activity));
        } else if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            timeTv.setTextSize((float) (TextSizePreferencesDao.get(activity) * 0.7));
        }

        batteryTv.setTextSize((float) (TextSizePreferencesDao.get(activity) * 0.13));
        battery2Tv.setTextSize((float) (TextSizePreferencesDao.get(activity) * 0.13));
        lunarTv.setTextSize((float) (TextSizePreferencesDao.get(activity) * 0.15));
        dateTv.setTextSize((float) (TextSizePreferencesDao.get(activity) * 0.2));
        weekTv.setTextSize((float) (TextSizePreferencesDao.get(activity) * 0.15));
        ampmTv.setTextSize((float) (TextSizePreferencesDao.get(activity) * 0.5));
        textSpaceTv.setTextSize((float) (TextSizePreferencesDao.get(activity) * 0.25));
        focusTimeTv.setTextSize((float) (TextSizePreferencesDao.get(activity) * 0.2));

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

        setTime(now, hour24, minute, hour12, second);

        if (dateTv != null)
            dateTv.setText(dateSDF.format(now));
        if (weekTv != null)
            weekTv.setText(weekSDF.format(now));

        LunarCalendar lunarCalendar = LunarCalendar.getInstance(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));

        lunarTv.setText(String.format("%s月%s", lunarCalendar.getLunarMonth(), lunarCalendar.getLunarDay()));


        if (FocusTimePreferencesDao.get(activity) > 0) {
            focusTimeTv.setText(diffDate(now, new Date(FocusTimePreferencesDao.get(activity))));
            focusTimeTv.setVisibility(View.VISIBLE);
        } else {
            focusTimeTv.setVisibility(View.GONE);
        }
        if (isWholeTime(minute, second)) {
            String file = getFileByTime(hour24, hour12);
            speak(file);
        }


    }

    private String diffDate(Date date1, Date date2) {
        String result;
        long diff = date2.getTime() - date1.getTime();
        if (diff < 0) {
            result = "「已过期」";
        } else if (diff < 1000) {
            result = "「已到达」";
        } else if (diff < 60 * 1000) {
            result = String.format("「剩余%s秒」", diff / 1000);
        } else if (diff < 60 * 1000 * 60) {
            result = String.format("「剩余%s分钟」", diff / 1000 / 60);
        } else if (diff < 60 * 1000 * 60 * 24) {
            result = String.format("「剩余%s小时」", diff / 1000 / 60 / 60);
        } else {
            result = String.format("「剩余%s天」", diff / 1000 / 60 / 60 / 24);
        }
        return result;
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
     * 根据时间获取声音文件.
     *
     * @param hour24
     * @param hour12
     * @return
     */
    private String getFileByTime(int hour24, int hour12) {
        if (hour24 == 0) hour24 = 24;
        if (hour12 == 0) hour12 = 12;
        String ampm = hour24 > 12 ? "pm" : "am";
        String filename = String.format("clock_%s%d", ampm, hour12);
        return filename;

    }

    /**
     * 设置时间.
     *
     * @param now
     * @param hour24
     * @param minute
     * @param hour12
     */
    private void setTime(Date now, int hour24, int minute, int hour12, int second) {
        if (Is12TimePreferencesDao.get(activity)) {
            if (timeTv != null) {
                Spannable spannable = getAmPmTextSpannable(hour24, minute, hour12, second);

                timeTv.setText(spannable);
            }

        } else {
            if (timeTv != null) {
                if (ShowSecondPreferencesDao.get(activity)) {
                    timeTv.setText(time24SDF.format(now));
                } else {
                    if (IsMaoHaoShanShuoPreferencesDao.get(activity)) {
                        if (second % 2 == 0) {
                            timeTv.setText(time24NoSecondSDF.format(now));
                        } else {
                            timeTv.setText(time24NoSecondNOMaoHaoSDF.format(now));
                        }
                    } else {
                        timeTv.setText(time24NoSecondSDF.format(now));
                    }
                }
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
    private Spannable getAmPmTextSpannable(int hour24, int minute, int hour12, int second) {
        if (hour12 == 0 && hour24 == 12) {
            hour12 = 12;
        }
        String ampm = hour24 >= 12 ? "PM" : "AM";
        String time = null;
        int start = 5;
        int end = 7;
        if (ShowSecondPreferencesDao.get(activity)) {
            time = String.format("%02d:%02d:%02d%s", hour12, minute, second, ampm);
            start = 8;
            end = 10;

        } else {
            if (IsMaoHaoShanShuoPreferencesDao.get(activity)) {
                if (second % 2 == 0) {
                    time = String.format("%02d:%02d%s", hour12, minute, ampm);
                } else {
                    time = String.format("%02d %02d%s", hour12, minute, ampm);
                }
            } else {
                time = String.format("%02d:%02d%s", hour12, minute, ampm);

            }
            start = 5;
            end = 7;
        }
        Spannable spannable = new SpannableString(time);
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
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                //电量的总刻度
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);

                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL;


                //把它转成百分比
                if (batteryTv != null) {
                    batteryTv.setText(String.format("%s:%d%%", isCharging ? "充电中" : "电量", (level * 100) / scale));
                    battery2Tv.setText(String.format("%s:%d%%", isCharging ? "充电中" : "电量", (level * 100) / scale));
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
                if (EnableProtectScreenPreferencesDao.get(activity) && (System.currentTimeMillis() - lastTime) > 1000 * 60 * 5) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) timeLl.getLayoutParams();
                    Random random = new Random();
                    int newTopMarginMax = centerRl.getHeight() <= 0 || timeLl.getHeight() <= 0 ? screenHeight / 2 : centerRl.getHeight() - timeLl.getHeight();

//                    Crashlytics.setInt("newTopMarginMax", newTopMarginMax);
                    params.topMargin = newTopMarginMax <= 0 ? 0 : random.nextInt(newTopMarginMax);
                    int newLeftMarginMax = centerRl.getWidth() <= 0 || timeLl.getWidth() <= 0 ? screenWidth / 2 : centerRl.getWidth() - timeLl.getWidth();

//                    Crashlytics.setInt("newLeftMarginMax", newLeftMarginMax);
                    params.leftMargin = newLeftMarginMax <= 0 ? 0 : random.nextInt(newLeftMarginMax);
                    timeLl.setLayoutParams(params);
                    params.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
                    lastTime = System.currentTimeMillis();


                    batteryTv.setVisibility(View.GONE);
                    battery2Tv.setVisibility(IsShowBatteryPreferencesDao.get(activity) ? View.VISIBLE : View.GONE);


                    RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) dateLl.getLayoutParams();
                    int align = random.nextInt(2);
                    Log.v("xxx", "params.topMargin:" + align);
                    params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
                    params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                    params1.addRule(align == 0 ? RelativeLayout.ALIGN_PARENT_LEFT : RelativeLayout.ALIGN_PARENT_RIGHT);
                    dateLl.setLayoutParams(params1);
                    dateLl.setGravity(align == 0 ? Gravity.LEFT : Gravity.RIGHT);


                }

            }
        }

    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {

            trans2Settings();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            if (distanceY > 30) {
//                currentLight = (int) (currentLight + (255 - currentLight)
//                        * distanceY / screenHeight);
//                currentLight = currentLight > 255 ? 255 : currentLight;
//                if (isHaveWriteSettinsPermisson()) {
//                    setScreenBrightness(currentLight);
//                    toast.show(activity, "亮度增加到:" + currentLight, activity.getWindow().getDecorView());
//                }
//            } else if (distanceY < -30) {
//                currentLight = (int) (currentLight - currentLight
//                        * (distanceY) / screenHeight);
//                currentLight = currentLight < 0 ? 0 : currentLight;
//                if (isHaveWriteSettinsPermisson()) {
//                    setScreenBrightness(currentLight);
//                    toast.show(activity, "亮度减弱到:" + currentLight, activity.getWindow().getDecorView());
//                }
//            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }
}
