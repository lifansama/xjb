package app.xunxun.homeclock.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import app.xunxun.homeclock.MyApplication;

/**
 * Created by fengdianxun on 2017/4/22.
 */

public class BaseActivity extends AppCompatActivity{

    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        MyApplication app = (MyApplication) getApplication();
        app.pushActivity(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
        MyApplication app = (MyApplication) getApplication();
        app.popActivity(this);
        super.onDestroy();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100){
            if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_DENIED) {
                showAlert("由于您拒绝了授权，检查更新功能将不能正常使用!");
            }
        }
    }
    private void showAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提醒");
        builder.setMessage(msg);
        builder.setPositiveButton("知道了", null);
        dialog = builder.show();

    }
}
