package app.xunxun.homeclock.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import android.widget.ImageView

import com.squareup.picasso.Picasso
import com.umeng.analytics.MobclickAgent

import app.xunxun.homeclock.R
import app.xunxun.homeclock.utils.FloatToast
import butterknife.ButterKnife
import butterknife.BindView

/**
 * 打赏页面.
 */
class SupportActivity : BaseActivity() {
    @BindView(R.id.alipay)
    internal var alipay: ImageView? = null
    @BindView(R.id.wechat)
    internal var wechat: ImageView? = null
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)
        ButterKnife.bind(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        alipay!!.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&qrcode=https://qr.alipay.com/FKX08261G0CFCMFFPUH102")
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        wechat!!.setOnClickListener { }
        Picasso.with(this).load(R.drawable.alipay).into(alipay)
        Picasso.with(this).load(R.drawable.wechat).into(wechat)
        countDownTimer = MyCountDown((60 * 1000).toLong(), 1000)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
        countDownTimer!!.start()
    }

    public override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
        countDownTimer!!.cancel()
    }

    internal inner class MyCountDown
    /**
     * @param millisInFuture    The number of millis in the future from the call
     * to [.start] until the countdown is done and [.onFinish]
     * is called.
     * @param countDownInterval The interval along the way to receive
     * [.onTick] callbacks.
     */
    (millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onTick(millisUntilFinished: Long) {
            title = String.format("打赏碗泡面给开发者(%s秒)", millisUntilFinished / 1000)

        }

        override fun onFinish() {
            onBackPressed()

        }
    }

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, SupportActivity::class.java))
        }
    }
}
