package app.xunxun.homeclock;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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

    private Timer timer;
    private TimerTask timerTask;
    private Handler handler;
    private SimpleDateFormat timeSDF = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat dateSDF = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat weekSDF = new SimpleDateFormat("E");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    Date now = new Date();
                    if (timeTv != null)
                        timeTv.setText(timeSDF.format(now));
                    if (dateTv != null)
                        dateTv.setText(dateSDF.format(now));
                    if (weekTv != null)
                        weekTv.setText(weekSDF.format(now));
                }
            }
        };
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(WHAT_TIME);
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    @Override
    public void onBackPressed() {
    }
}
