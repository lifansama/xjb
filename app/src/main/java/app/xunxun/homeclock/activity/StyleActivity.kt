package app.xunxun.homeclock.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.SeekBar
import android.widget.TextView
import app.xunxun.homeclock.EventNames
import app.xunxun.homeclock.R
import app.xunxun.homeclock.pref.MODE_COLOR
import app.xunxun.homeclock.pref.MODE_LOCAL_IMAGE
import app.xunxun.homeclock.pref.MODE_ONLINE_IMAGE
import app.xunxun.homeclock.pref.SimplePref
import app.xunxun.homeclock.preferences.*
import app.xunxun.homeclock.utils.RealPathUtil
import app.xunxun.homeclock.widget.ColorPickerDialog
import com.fourmob.colorpicker.ColorPickerSwatch
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_style.*
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange

class StyleActivity : BaseActivity() {
    private var backgroundColorPickerDialog: ColorPickerDialog? = null
    private var textColorPickerDialog: ColorPickerDialog? = null
    private var colors: IntArray? = null
    private var tempBackMode: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_style)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        colors = resources.getIntArray(R.array.colors)

        maohaoShanShuoCb!!.isChecked = IsMaoHaoShanShuoPreferencesDao.get(this)
        setShowDateCb(IsShowDatePreferencesDao.get(this))
        setShowLunarCb(IsShowLunarPreferencesDao.get(this))
        setShowWeekCb(IsShowWeekPreferencesDao.get(this))
        showBatteryCb.isChecked = IsShowBatteryPreferencesDao.get(this)

        tempBackMode = SimplePref.create(this).backgroundMode().get()
        renderBackModeRb()
        backgroundColorTv!!.setOnClickListener { backgroundColorPickerDialog!!.show() }
        textColorPickerDialog = ColorPickerDialog(this)
        textColorPickerDialog!!.initialize(R.string.txt_select_color, colors!!, TextColorPreferencesDao.get(this), 4, 2)

        backgroundColorPickerDialog = ColorPickerDialog(this)

        backgroundColorPickerDialog!!.initialize(R.string.txt_select_color, colors!!, SimplePref.create(this).backgroundColor().get(), 4, 2)
        backgroundColorPickerDialog!!.setOnColorSelectedListener(object : ColorPickerSwatch.OnColorSelectedListener {
            override fun onColorSelected(color: Int) {
                backgroundColorRb!!.isChecked = true
                SimplePref.create(this@StyleActivity).backgroundColor().set(color)
                MobclickAgent.onEvent(this@StyleActivity, EventNames.EVENT_CHANGE_BACKGROUND_COLOR)
            }
        })
        textColorPickerDialog!!.setOnColorSelectedListener(object : ColorPickerSwatch.OnColorSelectedListener {
            override fun onColorSelected(color: Int) {
                TextColorPreferencesDao.set(this@StyleActivity, color)
                MobclickAgent.onEvent(this@StyleActivity, EventNames.EVENT_CHANGE_TEXT_COLOR)
            }
        })
        textColorTv!!.setOnClickListener { textColorPickerDialog!!.show() }

        timeStyleRg!!.setOnCheckedChangeListener { radioGroup, id ->
            Is12TimePreferencesDao.set(radioGroup.context, id == R.id.time_12Rb)
        }

        showDateCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            IsShowDatePreferencesDao.set(buttonView.context, isChecked)
        }
        showLunarCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            IsShowLunarPreferencesDao.set(buttonView.context, isChecked)
        }
        showWeekCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            IsShowWeekPreferencesDao.set(buttonView.context, isChecked)
        }
        showBatteryCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            IsShowBatteryPreferencesDao.set(buttonView.context, isChecked)
        }

        textSizeTv!!.setOnClickListener {
            val linearLayout = LinearLayout(this@StyleActivity)
            linearLayout.setPadding(0, 64, 0, 64)
            linearLayout.orientation = LinearLayout.VERTICAL
            val seekBar = SeekBar(this@StyleActivity)
            seekBar.max = 300
            val textView = TextView(this@StyleActivity)
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                    textView.text = seekBar.progress.toString()
                    TextSizePreferencesDao.set(this@StyleActivity, seekBar.progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {

                }
            })
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.topMargin = 16
            seekBar.layoutParams = params

            textView.gravity = Gravity.CENTER
            textView.textSize = 32f
            textView.setTextColor(Color.BLACK)
            textView.text = TextSizePreferencesDao.get(this@StyleActivity).toString()
            seekBar.progress = TextSizePreferencesDao.get(this@StyleActivity)
            linearLayout.addView(textView)
            linearLayout.addView(seekBar)
            AlertDialog.Builder(this@StyleActivity).setView(linearLayout).show()
        }

        showSecondCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            ShowSecondPreferencesDao.set(buttonView.context, isChecked)
        }
        backgroundStyleRg.onCheckedChange { group, checkedId ->
            val rb = findViewById(checkedId) as RadioButton
            tempBackMode = SimplePref.create(group!!.context).backgroundMode().get()
            SimplePref.create(group!!.context).backgroundMode().set(Integer.parseInt(rb.tag as String))

            if (checkedId == R.id.backgroundPicRb) {
                alert("背景图片一天一换")
                backgroundColorTv.visibility = View.GONE
            } else if (checkedId == R.id.localBackgroundPicRb) {
                backgroundColorTv.visibility = View.GONE
                openGallery()
            } else {
                backgroundColorTv.visibility = View.VISIBLE
            }
        }

        if (Is12TimePreferencesDao.get(this))
            time_12Rb!!.isChecked = true
        else
            time_24Rb!!.isChecked = true

        showBatteryCb!!.isChecked = IsShowBatteryPreferencesDao.get(this)

        showSecondCb!!.isChecked = ShowSecondPreferencesDao.get(this)

        maohaoShanShuoCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            IsMaoHaoShanShuoPreferencesDao.set(buttonView.context, isChecked)
            if (isChecked) {
                alert("冒号闪烁需要在不显示秒针时起作用。")
            }
        }
    }

    private fun renderBackModeRb() {
        val mode = SimplePref.create(this).backgroundMode().get()
        if (mode == MODE_COLOR) {
            backgroundColorRb!!.isChecked = true
            backgroundColorTv.visibility = View.VISIBLE

        } else if (mode == MODE_ONLINE_IMAGE) {
            backgroundPicRb!!.isChecked = true
            backgroundColorTv.visibility = View.GONE
        } else if (mode == MODE_LOCAL_IMAGE) {
            localBackgroundPicRb!!.isChecked = true
            backgroundColorTv.visibility = View.GONE

        }
    }

    private fun setShowDateCb(isShow: Boolean) {
        showDateCb!!.isChecked = isShow

    }

    private fun setShowWeekCb(isShow: Boolean) {

        showWeekCb!!.isChecked = isShow
    }

    private fun setShowLunarCb(isShow: Boolean) {
        showLunarCb!!.isChecked = isShow

    }


    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        val intent = Intent.createChooser(galleryIntent, "选择图片")
        startActivityForResult(intent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data!!.data
                val path = RealPathUtil.getRealPathFromURI(this, uri)
                //                File newFile = Compressor.getDefault(this).compressToFile(new File(path));
                LocalImageFilePathPreferencesDao.set(this, path!!)
                alert("选图成功，后退查看效果")
            } else {
                SimplePref.create(this).backgroundMode().set(tempBackMode)
                renderBackModeRb()

            }
        }
    }

}

fun Activity.alert(msg: String) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle("温馨提醒")
    builder.setMessage(msg)
    builder.setPositiveButton("知道了", null)
    builder.show()

}
