package app.xunxun.homeclock.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import app.xunxun.homeclock.R
import app.xunxun.homeclock.pref.SimplePref
import app.xunxun.homeclock.preferences.FocusTimePreferencesDao
import app.xunxun.homeclock.preferences.TextSpaceContentPreferencesDao
import app.xunxun.homeclock.widget.DateTimePickerDialog
import app.xunxun.homeclock.widget.OnDateTimeSetListenner
import kotlinx.android.synthetic.main.activity_alert.*
import java.text.SimpleDateFormat
import java.util.*

class AlertActivity : BaseActivity() {
    private var dateTimePickerDialog: DateTimePickerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        dateTimePickerDialog = DateTimePickerDialog(this)
        dateTimePickerDialog!!.setOnDateTimeSetListenner(object : OnDateTimeSetListenner {
            override fun onDateTimeSeted(date: Date?) {

                if (date != null) {
                    FocusTimePreferencesDao.set(this@AlertActivity, date.time)

                    val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
                    setDateTv!!.text = String.format("到期时间:%s", simpleDateFormat.format(date))
                } else {
                    FocusTimePreferencesDao.set(this@AlertActivity, 0)
                    setDateTv!!.text = "点击设置到期时间"
                }
            }
        })

        if (FocusTimePreferencesDao.get(this@AlertActivity) > 0) {
            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
            setDateTv!!.text = String.format("到期时间:%s", simpleDateFormat.format(Date(FocusTimePreferencesDao.get(this@AlertActivity))))
        } else {
            setDateTv!!.text = "点击设置到期时间"
        }
        setDateTv!!.setOnClickListener { dateTimePickerDialog!!.show() }
        if (!TextUtils.isEmpty(TextSpaceContentPreferencesDao.get(this))) {
            textSpaceEt!!.setText(TextSpaceContentPreferencesDao.get(this))
        }
        textSpaceEt!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                TextSpaceContentPreferencesDao.set(this@AlertActivity, s.toString())

            }
        })
    }
}
