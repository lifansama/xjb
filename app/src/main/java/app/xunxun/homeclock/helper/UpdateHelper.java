package app.xunxun.homeclock.helper;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import app.xunxun.homeclock.utils.FloatToast;

/**
 * Created by fengdianxun on 2017/5/2.
 */

public class UpdateHelper {
    private Activity mActivity;

    public UpdateHelper(Activity activity) {
        this.mActivity = activity;
    }

    public void check(final boolean force) {

        int flag = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (PackageManager.PERMISSION_GRANTED == flag) {
            PgyUpdateManager.register(mActivity, "app.xunxun.homeclock",
                    new UpdateManagerListener() {

                        @Override
                        public void onUpdateAvailable(final String result) {

                            // 将新版本信息封装到AppBean中
                            if (mActivity != null && !mActivity.isFinishing()) {
                                final AppBean appBean = getAppBeanFromString(result);
                                new AlertDialog.Builder(mActivity)
                                        .setTitle("更新")
                                        .setMessage(appBean.getReleaseNote())
                                        .setPositiveButton(
                                                "确定",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
                                                        startDownloadTask(
                                                                mActivity,
                                                                appBean.getDownloadURL());
                                                    }
                                                })
                                        .setNegativeButton("取消", null)
                                        .show();
                            }
                            PgyUpdateManager.unregister();

                        }

                        @Override
                        public void onNoUpdateAvailable() {
                            if (force) {
                                FloatToast floatToast = new FloatToast();
                                floatToast.show(mActivity, "已是最新版", mActivity.getWindow().getDecorView());
                            }
                            PgyUpdateManager.unregister();
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }


    }
}
