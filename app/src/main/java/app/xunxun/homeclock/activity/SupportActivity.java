package app.xunxun.homeclock.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import app.xunxun.homeclock.R;
import app.xunxun.homeclock.utils.FloatToast;
import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * 打赏页面.
 */
public class SupportActivity extends BaseActivity {
    @BindView(R.id.alipay)
    ImageView alipay;
    @BindView(R.id.wechat)
    ImageView wechat;
    private CountDownTimer countDownTimer;

    public static void start(Context context) {
        context.startActivity(new Intent(context, SupportActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=https://qr.alipay.com/FKX08261G0CFCMFFPUH102"));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Picasso.with(this).load(R.drawable.alipay).into(alipay);
        Picasso.with(this).load(R.drawable.wechat).into(wechat);
        countDownTimer = new MyCountDown(60*1000,1000);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        countDownTimer.start();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        countDownTimer.cancel();
    }

    class MyCountDown extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setTitle(String.format("打赏碗泡面给开发者(%s秒)", millisUntilFinished / 1000));

        }

        @Override
        public void onFinish() {
            onBackPressed();

        }
    }
}
