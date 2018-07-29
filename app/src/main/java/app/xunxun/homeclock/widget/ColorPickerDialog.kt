package app.xunxun.homeclock.widget

import android.app.Activity
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.fourmob.colorpicker.ColorPickerPalette
import com.fourmob.colorpicker.ColorPickerSwatch

/**
 * Created by fengdianxun on 2017/6/1.
 */

class ColorPickerDialog(private val activity: Activity) {

    private val alertDialog: AlertDialog
    private val palette: ColorPickerPalette
    private var onColorSelectedListener: ColorPickerSwatch.OnColorSelectedListener? = null
    private val titleView: TextView

    init {
        val builder = AlertDialog.Builder(activity)
        val view = LayoutInflater.from(activity).inflate(com.fourmob.colorpicker.R.layout.color_picker_dialog, null)
        palette = view.findViewById(com.fourmob.colorpicker.R.id.color_picker) as ColorPickerPalette
        builder.setView(view)
        titleView = TextView(activity)
        titleView.setBackgroundColor(Color.LTGRAY)
        titleView.textSize = 24f
        titleView.setTextColor(Color.BLACK)
        titleView.setPadding(64, 64, 64, 0)
        builder.setCustomTitle(titleView)
        view.setBackgroundColor(Color.LTGRAY)

        alertDialog = builder.create()
    }

    fun show() {

        alertDialog.show()
    }


    fun initialize(titleId: Int, colors: IntArray, selectedColor: Int, columns: Int, size: Int) {
        palette.init(size, columns) { color ->
            if (onColorSelectedListener != null) {
                onColorSelectedListener!!.onColorSelected(color)
                palette.drawPalette(colors, color)
            }
            alertDialog.dismiss()
        }
        titleView.setText(titleId)
        palette.drawPalette(colors, selectedColor)
    }

    fun setOnColorSelectedListener(onColorSelectedListener: ColorPickerSwatch.OnColorSelectedListener) {
        this.onColorSelectedListener = onColorSelectedListener
    }

}
