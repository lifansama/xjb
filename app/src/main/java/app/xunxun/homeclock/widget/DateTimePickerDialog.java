package app.xunxun.homeclock.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import app.xunxun.homeclock.R;

/**
 * Created by fengdianxun on 2015/4/25.
 */
public class DateTimePickerDialog {
    private OnDateTimeSetListenner onDateTimeSetListenner;
    private CharSequence positin = "确定";
    private CharSequence negative = "取消";
    private final DatePicker datePicker;
    private final TimePicker timePicker;
    private AlertDialog alertDialog;

    public DateTimePickerDialog(Activity context) {
        this(context, null);
    }

    public DateTimePickerDialog(Activity activity, Date date) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        View view = LayoutInflater.from(activity).inflate(R.layout.view_date_time_picker, null, false);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        long datenowL = calendar.getTimeInMillis();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            datePicker.setMinDate(datenowL);
        }
        if (date == null) {
            date = new Date();
        }
        calendar.setTime(date);
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        } else {
            timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setMinute(calendar.get(Calendar.MINUTE));
        } else {
            timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));

        }
        builder.setView(view);
        builder.setPositiveButton(positin, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar calendar = Calendar.getInstance();
                int hour;
                int minute;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                } else {
                    hour = timePicker.getCurrentHour();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    minute = timePicker.getMinute();
                } else {
                    minute = timePicker.getCurrentMinute();
                }

                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), hour, minute);
                if (onDateTimeSetListenner != null)
                    onDateTimeSetListenner.onDateTimeSeted(calendar.getTime());

            }
        });
        builder.setNegativeButton(negative, null);
        builder.setNeutralButton("清除选择", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onDateTimeSetListenner != null){
                    onDateTimeSetListenner.onDateTimeSeted(null);
                }

            }
        });
        alertDialog = builder.create();

    }

    public void setOnDateTimeSetListenner(OnDateTimeSetListenner onDateTimeSetListenner) {
        this.onDateTimeSetListenner = onDateTimeSetListenner;
    }

    public void setPositin(CharSequence positin) {
        this.positin = positin;
    }

    public void setNegative(CharSequence negative) {
        this.negative = negative;
    }


    public void show() {
        alertDialog.show();
    }
}
