package app.xunxun.homeclock.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import app.xunxun.homeclock.ClockViewController
import app.xunxun.homeclock.R
import app.xunxun.homeclock.pref.SimplePref
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_time.view.*
import org.jetbrains.anko.forEachChild
import org.jetbrains.anko.textColor
import java.util.*

class TimeView(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    var is12Hour: Boolean = false
    var showSecond: Boolean = true
    var maohaoShanShuo: Boolean = false

    init {
      LayoutInflater.from(context).inflate(R.layout.view_time, this, true)
    }

    fun setTypeface(typeface: Typeface) {
        hourTv.typeface = typeface
        maohao1Tv.typeface = typeface
        minuteTv.typeface = typeface
        maohao2Tv.typeface = typeface
        secondTv.typeface = typeface
    }

    fun setTimeTextSize(textsize: Float) {
        hourTv.textSize = textsize
        maohao1Tv.textSize = textsize
        minuteTv.textSize = textsize
        maohao2Tv.textSize = textsize
        secondTv.textSize = textsize
    }

    fun setAmpmTextSize(textsize: Float){
        ampmTv.textSize = textsize
    }

    fun setTextColor(color: Int) {
        timeLl.forEachChild { (it as TextView).textColor = color }

    }

    fun setTime() {
        is12Hour = SimplePref.create(context).is12Time().get()
        showSecond = SimplePref.create(context).showSecond().get()
        maohaoShanShuo = SimplePref.create(context).isMaoHaoShanShuo().get()

        val calendar = Calendar.getInstance()
        val hour24 = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val hour12 = calendar.get(Calendar.HOUR)

        secondTv.visibility = if (showSecond) View.VISIBLE else View.GONE
        secondTv.text = String.format("%02d",second)
        minuteTv.text = String.format("%02d",minute)
        hourTv.text = String.format("%02d",if (is12Hour) hour12 else hour24)
        maohao1Tv.visibility = if (maohaoShanShuo && second % 2 == 0) View.INVISIBLE else View.VISIBLE
        maohao2Tv.visibility = if (showSecond) View.VISIBLE else View.GONE
        ampmTv.text = if (hour24 >= 12) "PM" else "AM"
        ampmTv.visibility = if (is12Hour) View.VISIBLE else View.GONE

    }
}