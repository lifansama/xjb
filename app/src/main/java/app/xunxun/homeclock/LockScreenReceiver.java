package app.xunxun.homeclock;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import app.xunxun.homeclock.activity.MainActivity;
import io.github.xhinliang.lunarcalendar.Main;

/**
 * Created by fengdianxun on 2017/4/21.
 */

public class LockScreenReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("LockScreenReceiver", "onReceive: " + intent.getAction());
        if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);

        }else if (intent.getAction().equals("android.intent.action.USER_PRESENT")){
            Intent intent1 = new Intent(context, MainActivity.class);
            intent1.setAction("app.xunxun.homeclock.finish.activity");
            context.startActivity(intent1);
        }
    }
}
