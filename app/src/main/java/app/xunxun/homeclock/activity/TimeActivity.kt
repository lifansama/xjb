package app.xunxun.homeclock.activity

import android.os.Bundle
import app.xunxun.homeclock.R
import app.xunxun.homeclock.pref.SimplePref
import kotlinx.android.synthetic.main.activity_time.*
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange

/**
 * 整点报时.
 */
class TimeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        enableSpeakWholeTimeCb!!.isChecked = SimplePref.create(this).enableVibrateWholeTime().get()
        enableVoiceWholeTimeCb!!.isChecked = SimplePref.create(this).enableVoiceWholeTime().get()
        enableSpeakWholeTimeCb.onCheckedChange { buttonView, isChecked ->
            SimplePref.create(buttonView!!.context).enableVibrateWholeTime().set(isChecked)
        }
        enableVoiceWholeTimeCb.onCheckedChange { buttonView, isChecked ->
            SimplePref.create(buttonView!!.context).enableVoiceWholeTime().set(isChecked)
        }
    }
}
