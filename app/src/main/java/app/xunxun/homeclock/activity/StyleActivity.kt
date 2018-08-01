package app.xunxun.homeclock.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import app.xunxun.homeclock.EventNames
import app.xunxun.homeclock.R
import app.xunxun.homeclock.preferences.*
import app.xunxun.homeclock.utils.RealPathUtil
import app.xunxun.homeclock.widget.ColorPickerDialog
import com.fourmob.colorpicker.ColorPickerSwatch
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_func.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_style.*

class StyleActivity : BaseActivity() {
    private var backgroundColorPickerDialog: ColorPickerDialog? = null
    private var textColorPickerDialog: ColorPickerDialog? = null
    private var colors: IntArray? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_style)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        colors = resources.getIntArray(R.array.colors)

        val mode = BackgroundModePreferencesDao.get(this)
        if (mode == BackgroundModePreferencesDao.MODE_COLOR) {
            backgroundColorRb!!.isChecked = true

        } else if (mode == BackgroundModePreferencesDao.MODE_ONLINE_IMAGE) {
            backgroundPicRb!!.isChecked = true
        } else if (mode == BackgroundModePreferencesDao.MODE_LOCAL_IMAGE) {
            localBackgroundPicRb!!.isChecked = true
        }
        backgroundColorTv!!.setOnClickListener { backgroundColorPickerDialog!!.show() }
        textColorPickerDialog = ColorPickerDialog(this)
        textColorPickerDialog!!.initialize(R.string.txt_select_color, colors!!, TextColorPreferencesDao.get(this), 4, 2)

        backgroundColorPickerDialog = ColorPickerDialog(this)

        backgroundColorPickerDialog!!.initialize(R.string.txt_select_color, colors!!, BackgroundColorPreferencesDao.get(this), 4, 2)
        backgroundColorPickerDialog!!.setOnColorSelectedListener(object : ColorPickerSwatch.OnColorSelectedListener {
            override fun onColorSelected(color: Int) {
                backgroundColorRb!!.isChecked = true
                BackgroundColorPreferencesDao.set(this@StyleActivity, color)
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

            showSecondCb!!.setOnCheckedChangeListener { buttonView, isChecked ->
                ShowSecondPreferencesDao.set(buttonView.context, isChecked)
            }
            backgroundStyleRg!!.setOnCheckedChangeListener { group, checkedId ->
                val rb = findViewById(checkedId) as RadioButton
                BackgroundModePreferencesDao.set(group.context, Integer.parseInt(rb.tag as String))

                if (checkedId == R.id.backgroundPicRb) {
                    showAlert("背景图片一天一换")
                } else if (checkedId == R.id.localBackgroundPicRb) {
                    openGallery()
                }
            }

            if (Is12TimePreferencesDao.get(this))
                time_12Rb!!.isChecked = true
            else
                time_24Rb!!.isChecked = true

            showBatteryCb!!.isChecked = IsShowBatteryPreferencesDao.get(this)

            showSecondCb!!.isChecked = ShowSecondPreferencesDao.get(this)
        }
    }

    private fun showAlert(msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("温馨提醒")
        builder.setMessage(msg)
        builder.setPositiveButton("知道了", null)
        builder.show()

    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        val intent = Intent.createChooser(galleryIntent, "选择图片")
        startActivityForResult(intent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data.data
                val path = RealPathUtil.getRealPathFromURI(this, uri)
                //                File newFile = Compressor.getDefault(this).compressToFile(new File(path));
                LocalImageFilePathPreferencesDao.set(this, path!!)
                Toast.makeText(this, "选图成功，后退查看效果", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
