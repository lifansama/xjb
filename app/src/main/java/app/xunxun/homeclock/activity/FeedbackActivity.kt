package app.xunxun.homeclock.activity

import android.os.Bundle
import app.xunxun.homeclock.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_feedback.*

class FeedbackActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Picasso.with(this).load(R.drawable.mmqrcode).into(qrcode)
    }
}
