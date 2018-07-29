package app.xunxun.homeclock.utils

import android.app.Activity

import java.util.Timer
import java.util.TimerTask

/**
 * 用于双击退出的封装方法
 */
class DoubleClickExit(private val activity: Activity?//传入的Activity
) {
    internal var isExit: Boolean = false//判断是否退出
    private val floatToast: FloatToast

    init {
        isExit = false
        floatToast = FloatToast()
    }

    /**
     * 用于双击退出，单击显示Toast提示
     * 每次点击判断isExit的状态，为真则直接退出，
     * 为假则给出提示，设置isExit为真，两秒后设置isExit为假
     */
    fun doubleClickExit() {
        var tTask: Timer? = null
        if (!isExit) {
            isExit = true
            if (activity != null && !activity.isFinishing) {
                floatToast.show(activity, "再按一次退出程序", activity.window.decorView)
            }
            tTask = Timer()
            tTask.schedule(object : TimerTask() {
                override fun run() {
                    isExit = false
                }
            }, TIME.toLong())
        } else {
            activity!!.finish()
        }
    }

    companion object {
        private val TIME = 2000//间隔的时间
    }
}
