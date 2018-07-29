package app.xunxun.homeclock.utils

import android.app.Activity
import android.os.Build
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast

/**
 * Created by fengdianxun on 2017/4/22.
 */

class FloatToast {


    private var popupWindow: PopupWindow? = null
    private val handler = Handler()

    fun show(activity: Activity?, text: String, view: View) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (popupWindow == null)
                popupWindow = PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            if (activity != null && !activity.isFinishing) {
                if (popupWindow!!.isShowing) {
                    popupWindow!!.dismiss()
                }
                val textView = TextView(activity)
                textView.textSize = 16f
                textView.gravity = Gravity.CENTER
                textView.setPadding(0, 16, 0, 16)
                textView.text = text
                popupWindow!!.contentView = textView
                popupWindow!!.showAtLocation(view, Gravity.BOTTOM, 0, 0)
                handler.postDelayed({
                    if (activity != null && !activity.isFinishing && popupWindow != null && popupWindow!!.isShowing) {
                        popupWindow!!.dismiss()
                    }
                }, 2000)
            }

        } else {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
        }
    }


}
