package app.xunxun.homeclock;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import app.xunxun.homeclock.activity.SettingsActivity;
import app.xunxun.homeclock.preferences.LockScreenShowOnPreferencesDao;
import app.xunxun.homeclock.preferences.TextSpaceContentPreferencesDao;

public class MyService extends Service {

    private BroadcastReceiver receiver;

    public static void startService(Context context) {
        context.startService(new Intent(context, MyService.class));

    }

    public static void stopService(Context context) {
        context.stopService(new Intent(context, MyService.class));
    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new LockScreenReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (LockScreenShowOnPreferencesDao.get(this)) {
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.activity_notify);
            String text = TextUtils.isEmpty(TextSpaceContentPreferencesDao.get(this)) ? "点击写下提醒" : TextSpaceContentPreferencesDao.get(this);
            remoteViews.setTextViewText(R.id.textSpaceTv, text);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(this, SettingsActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.rootFl, pendingIntent);
            Notification notification = new NotificationCompat.Builder(this)
                    .setCustomContentView(remoteViews)
                    .setCustomBigContentView(remoteViews)
                    .setSmallIcon(R.mipmap.ic_app)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);
        }
        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        if (LockScreenShowOnPreferencesDao.get(this)) {
            stopForeground(true);
        }
    }
}
