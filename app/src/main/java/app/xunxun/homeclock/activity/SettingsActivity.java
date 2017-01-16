package app.xunxun.homeclock.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fourmob.colorpicker.ColorPickerDialog;
import com.fourmob.colorpicker.ColorPickerSwatch;
import com.umeng.analytics.MobclickAgent;

import app.xunxun.homeclock.EventNames;
import app.xunxun.homeclock.R;
import app.xunxun.homeclock.preferences.BackgroundColorPreferencesDao;
import app.xunxun.homeclock.preferences.KeepScreenOnPreferencesDao;
import app.xunxun.homeclock.preferences.TextColorPreferencesDao;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingsActivity extends AppCompatActivity {
    @InjectView(R.id.backgroundColorTv)
    TextView backgroundColorTv;
    @InjectView(R.id.timeTv)
    TextView timeTv;
    @InjectView(R.id.dateTv)
    TextView dateTv;
    @InjectView(R.id.weekTv)
    TextView weekTv;
    @InjectView(R.id.backRl)
    RelativeLayout backRl;
    @InjectView(R.id.textColorTv)
    TextView textColorTv;
    @InjectView(R.id.textSizeTv)
    TextView textSizeTv;
    @InjectView(R.id.activity_settings)
    LinearLayout activitySettings;
    @InjectView(R.id.supportTv)
    TextView supportTv;
    @InjectView(R.id.keepScreenOnCb)
    CheckBox keepScreenOnCb;
    private ColorPickerDialog backgroundColorPickerDialog;
    private ColorPickerDialog textColorPickerDialog;

    public static void start(Context context) {
        context.startActivity(new Intent(context, SettingsActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        backgroundColorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backgroundColorPickerDialog.show(getSupportFragmentManager(), "colorpicker1");
            }
        });
        backgroundColorPickerDialog = new ColorPickerDialog();
        int[] colors = getResources().getIntArray(R.array.colors);
        backgroundColorPickerDialog.initialize(R.string.txt_select_color, colors, BackgroundColorPreferencesDao.get(this), 4, 2);
        backgroundColorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                backRl.setBackgroundColor(color);
                BackgroundColorPreferencesDao.set(SettingsActivity.this, color);
                MobclickAgent.onEvent(SettingsActivity.this, EventNames.EVENT_CHANGE_BACKGROUND_COLOR);
            }
        });
        textColorPickerDialog = new ColorPickerDialog();
        textColorPickerDialog.initialize(R.string.txt_select_color, colors, TextColorPreferencesDao.get(this), 4, 2);
        textColorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                timeTv.setTextColor(color);
                dateTv.setTextColor(color);
                weekTv.setTextColor(color);
                TextColorPreferencesDao.set(SettingsActivity.this, color);
                MobclickAgent.onEvent(SettingsActivity.this, EventNames.EVENT_CHANGE_TEXT_COLOR);
            }
        });
        textColorTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textColorPickerDialog.show(getSupportFragmentManager(), "colorpicker2");

            }
        });
        textSizeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        supportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SupportActivity.start(view.getContext());
            }
        });
        backRl.setBackgroundColor(BackgroundColorPreferencesDao.get(this));
        timeTv.setTextColor(TextColorPreferencesDao.get(this));
        dateTv.setTextColor(TextColorPreferencesDao.get(this));
        weekTv.setTextColor(TextColorPreferencesDao.get(this));
        keepScreenOnCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                KeepScreenOnPreferencesDao.set(compoundButton.getContext(),isCheck);
            }
        });
        keepScreenOnCb.setChecked(KeepScreenOnPreferencesDao.get(this));


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        MainActivity.start(this);
        finish();
    }
}
