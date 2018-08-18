package app.xunxun.homeclock

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.text.TextUtils
import android.widget.RemoteViews

import app.xunxun.homeclock.activity.SettingsActivity
import app.xunxun.homeclock.pref.SimplePref

class MyService : Service() {

    private var receiver: BroadcastReceiver? = null

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        receiver = LockScreenReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.SCREEN_ON")
        intentFilter.addAction("android.intent.action.SCREEN_OFF")
        intentFilter.addAction("android.intent.action.USER_PRESENT")
        registerReceiver(receiver, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (SimplePref.create(this).lockScreenShowOn().get()) {
            val remoteViews = RemoteViews(packageName, R.layout.activity_notify)
            val text = if (TextUtils.isEmpty(SimplePref.create(this).textSpaceContent().get())) "点击写下提醒" else
                SimplePref.create(this).textSpaceContent().get()
            remoteViews.setTextViewText(R.id.textSpaceTv, text)
            val pendingIntent = PendingIntent.getActivity(this, 1, Intent(this, SettingsActivity::class.java), PendingIntent.FLAG_CANCEL_CURRENT)
            remoteViews.setOnClickPendingIntent(R.id.rootFl, pendingIntent)
            val notification = NotificationCompat.Builder(this)
                    .setCustomContentView(remoteViews)
                    .setCustomBigContentView(remoteViews)
                    .setSmallIcon(R.mipmap.ic_app)
                    .setContentIntent(pendingIntent)
                    .build()
            startForeground(1, notification)
        }
        return Service.START_STICKY_COMPATIBILITY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        if (!SimplePref.create(this).notifyStay().get() || SimplePref.create(this).lockScreenShowOn().get()) {
            stopForeground(true)
        }
    }

    companion object {

        fun startService(context: Context) {
            if (SimplePref.create(context).notifyStay().get()) {
                context.startService(Intent(context, MyService::class.java))
            }

        }

        fun stopService(context: Context) {
            if (!SimplePref.create(context).notifyStay().get()) {
                context.stopService(Intent(context, MyService::class.java))
            }
        }
    }
}
