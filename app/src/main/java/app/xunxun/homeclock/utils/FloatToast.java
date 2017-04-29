package app.xunxun.homeclock.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by fengdianxun on 2017/4/22.
 */

public class FloatToast {


    private PopupWindow popupWindow;
    private Handler handler = new Handler();

    public void show(final Activity activity, String text, View view) {

        popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(activity);
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(0,16,0,16);
        textView.setText(text);
        popupWindow.setContentView(textView);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (activity != null && !activity.isFinishing()&&popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        }, 2000);
    }


}
