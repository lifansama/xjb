package app.xunxun.homeclock.utils;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by fengdianxun on 2017/4/22.
 */

public class FloatToast {


    private PopupWindow popupWindow;
    private Handler handler = new Handler();

    public FloatToast() {
        popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void show(final Activity activity, String text, View view) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

            if (activity != null && !activity.isFinishing()) {
                if (popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                TextView textView = new TextView(activity);
                textView.setTextSize(16);
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(0, 16, 0, 16);
                textView.setText(text);
                popupWindow.setContentView(textView);
                popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (activity != null && !activity.isFinishing() && popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    }
                }, 2000);
            }

        } else {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        }
    }


}
