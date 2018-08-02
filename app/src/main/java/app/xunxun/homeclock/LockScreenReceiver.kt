package app.xunxun.homeclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log

import app.xunxun.homeclock.activity.MainActivity

/**
 * Created by fengdianxun on 2017/4/21.
 */

class LockScreenReceiver : BroadcastReceiver() {
    private var lastAction: String? = null

    override fun onReceive(context: Context, intent: Intent) {

        Log.v("LockScreenReceiver", "onReceive: ${intent.action}")
        Log.v("LockScreenReceiver", "onReceive lastAction:$lastAction ")
        if (intent.action == "android.intent.action.SCREEN_OFF") {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (telephonyManager.callState == TelephonyManager.CALL_STATE_IDLE) {
                if (lastAction != null && lastAction == "android.intent.action.USER_PRESENT") {
                    val app = context.applicationContext as MyApplication
                    app.clearActivities()
                } else {
                    val intent1 = Intent(context, MainActivity::class.java)
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent1)

                }
            }

        } else if (intent.action == "android.intent.action.USER_PRESENT") {
            if (lastAction != null && lastAction == "android.intent.action.SCREEN_OFF") {
                val app = context.applicationContext as MyApplication
                app.clearActivities()
            }
        }
        lastAction = intent.action
    }
}
