package app.xunxun.homeclock.widget;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fourmob.colorpicker.ColorPickerPalette;
import com.fourmob.colorpicker.ColorPickerSwatch;

/**
 * Created by fengdianxun on 2017/6/1.
 */

public class ColorPickerDialog {

    private AlertDialog alertDialog;
    private Activity activity;
    private final ColorPickerPalette palette;
    private ColorPickerSwatch.OnColorSelectedListener onColorSelectedListener;
    private final TextView titleView;

    public ColorPickerDialog(Activity activity) {
        this.activity = activity;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(com.fourmob.colorpicker.R.layout.color_picker_dialog, null);
        palette = ((ColorPickerPalette) view.findViewById(com.fourmob.colorpicker.R.id.color_picker));
        builder.setView(view);
        titleView = new TextView(activity);
        titleView.setBackgroundColor(Color.LTGRAY);
        titleView.setTextSize(24);
        titleView.setTextColor(Color.BLACK);
        titleView.setPadding(64,64,64,0);
        builder.setCustomTitle(titleView);
        view.setBackgroundColor(Color.LTGRAY);

        alertDialog = builder.create();
    }

    public void show(){

        alertDialog.show();
    }


    public void initialize(int titleId, final int[] colors, int selectedColor, int columns, int size) {
        palette.init(size, columns, new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                if (onColorSelectedListener != null){
                    onColorSelectedListener.onColorSelected(color);
                    palette.drawPalette(colors,color);
                }
                alertDialog.dismiss();


            }
        });
        titleView.setText(titleId);
        palette.drawPalette(colors,selectedColor);
    }

    public void setOnColorSelectedListener(ColorPickerSwatch.OnColorSelectedListener onColorSelectedListener){
        this.onColorSelectedListener = onColorSelectedListener;
    }

}
