package app.xunxun.homeclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import app.xunxun.homeclock.activity.MainActivity;

/**
 * Created by fengdianxun on 2017/4/21.
 */

public class LockScreenReceiver extends BroadcastReceiver {
    private String lastAction;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("LockScreenReceiver", "onReceive: " + intent.getAction());
        Log.v("LockScreenReceiver", "onReceive lastAction: " + lastAction);
        if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
            if (lastAction != null && lastAction.equals("android.intent.action.USER_PRESENT")) {
                MyApplication app = (MyApplication) context.getApplicationContext();
                app.clearActivities();
            } else {
                Intent intent1 = new Intent(context, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent1);

            }

        } else if (intent.getAction().equals("android.intent.action.USER_PRESENT")) {
            if (lastAction != null && lastAction.equals("android.intent.action.SCREEN_OFF")) {
                MyApplication app = (MyApplication) context.getApplicationContext();
                app.clearActivities();
            }
        }
        lastAction = intent.getAction();
    }
}
