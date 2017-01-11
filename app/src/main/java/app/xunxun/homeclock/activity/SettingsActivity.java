package app.xunxun.homeclock.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fourmob.colorpicker.ColorPickerDialog;
import com.fourmob.colorpicker.ColorPickerSwatch;

import app.xunxun.homeclock.R;
import app.xunxun.homeclock.preferences.BackgroundColorPreferencesDao;
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


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
