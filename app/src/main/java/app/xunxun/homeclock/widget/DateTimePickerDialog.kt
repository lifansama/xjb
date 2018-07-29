package app.xunxun.homeclock.widget

import android.app.Activity
import android.content.DialogInterface
import android.os.Build
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker

import java.util.Calendar
import java.util.Date

import app.xunxun.homeclock.R

/**
 * Created by fengdianxun on 2015/4/25.
 */
class DateTimePickerDialog(activity: Activity, date: Date?) {
    private var onDateTimeSetListenner: OnDateTimeSetListenner? = null
    private var positin: CharSequence = "确定"
    private var negative: CharSequence = "取消"
    private val datePicker: DatePicker
    private val timePicker: TimePicker
    private val alertDialog: AlertDialog

    constructor(context: Activity) : this(context, null) {}

    init {
        var date = date
        val builder = AlertDialog.Builder(activity)

        val view = LayoutInflater.from(activity).inflate(R.layout.view_date_time_picker, null, false)
        datePicker = view.findViewById(R.id.datePicker) as DatePicker
        timePicker = view.findViewById(R.id.timePicker) as TimePicker

        val calendar = Calendar.getInstance()
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
        val datenowL = calendar.timeInMillis
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            datePicker.minDate = datenowL
        }
        if (date == null) {
            date = Date()
        }
        calendar.time = date
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour = calendar.get(Calendar.HOUR_OF_DAY)
        } else {
            timePicker.currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.minute = calendar.get(Calendar.MINUTE)
        } else {
            timePicker.currentMinute = calendar.get(Calendar.MINUTE)

        }
        builder.setView(view)
        builder.setPositiveButton(positin) { dialog, which ->
            val calendar = Calendar.getInstance()
            val hour: Int
            val minute: Int
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hour = timePicker.hour
            } else {
                hour = timePicker.currentHour
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                minute = timePicker.minute
            } else {
                minute = timePicker.currentMinute
            }

            calendar.set(datePicker.year, datePicker.month, datePicker.dayOfMonth, hour, minute)
            if (onDateTimeSetListenner != null)
                onDateTimeSetListenner!!.onDateTimeSeted(calendar.time)
        }
        builder.setNegativeButton(negative, null)
        builder.setNeutralButton("清除选择") { dialog, which ->
            if (onDateTimeSetListenner != null) {
                onDateTimeSetListenner!!.onDateTimeSeted(null)
            }
        }
        alertDialog = builder.create()

    }

    fun setOnDateTimeSetListenner(onDateTimeSetListenner: OnDateTimeSetListenner) {
        this.onDateTimeSetListenner = onDateTimeSetListenner
    }

    fun setPositin(positin: CharSequence) {
        this.positin = positin
    }

    fun setNegative(negative: CharSequence) {
        this.negative = negative
    }


    fun show() {
        alertDialog.show()
    }
}
