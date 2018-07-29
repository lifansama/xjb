package app.xunxun.homeclock

import android.app.Notification
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
import app.xunxun.homeclock.preferences.LockScreenShowOnPreferencesDao
import app.xunxun.homeclock.preferences.NotifyStayPreferencesDao
import app.xunxun.homeclock.preferences.TextSpaceContentPreferencesDao

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

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (LockScreenShowOnPreferencesDao.get(this)) {
            val remoteViews = RemoteViews(packageName, R.layout.activity_notify)
            val text = if (TextUtils.isEmpty(TextSpaceContentPreferencesDao.get(this))) "点击写下提醒" else TextSpaceContentPreferencesDao.get(this)
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
        if (!NotifyStayPreferencesDao.get(this) || LockScreenShowOnPreferencesDao.get(this)) {
            stopForeground(true)
        }
    }

    companion object {

        fun startService(context: Context) {
            if (NotifyStayPreferencesDao.get(context)) {
                context.startService(Intent(context, MyService::class.java))
            }

        }

        fun stopService(context: Context) {
            if (!NotifyStayPreferencesDao.get(context)) {
                context.stopService(Intent(context, MyService::class.java))
            }
        }
    }
}
