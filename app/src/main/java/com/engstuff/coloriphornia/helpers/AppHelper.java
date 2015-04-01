package com.engstuff.coloriphornia.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.FullScreenColorC;
import com.engstuff.coloriphornia.data.Cv;

public class AppHelper {

    public static void startFullColorC(Context ctx, String rgb, String hex) {

        String[] colorParams = {rgb, hex};

        Intent i = new Intent(ctx, FullScreenColorC.class);

        i.putExtra(Cv.EXTRA_MESSAGE_COLOR_1, colorParams);
        i.putExtra(Cv.EXTRA_MESSAGE_TEXT_COLOR_1, ColorParams.blackOrWhiteText(hex));

        ctx.startActivity(i);
    }

    public static void showCustomToast(Activity a, String hexColorParams, int colorHex) {

        Toast toast = new Toast(a);

        TextView view = (TextView)
                a.getLayoutInflater().inflate(R.layout.toast_custom, null);

        view.setBackgroundColor(colorHex);

        view.setTextColor(ColorParams.blackOrWhiteText(hexColorParams) ? Color.WHITE : Color.BLACK);
        view.setText("This color's been saved\n\n            " + hexColorParams);

        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }
}
