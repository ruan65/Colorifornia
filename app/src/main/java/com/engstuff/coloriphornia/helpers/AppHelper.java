package com.engstuff.coloriphornia.helpers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.engstuff.coloriphornia.R;
import com.engstuff.coloriphornia.activities.FullScreenColorC;
import com.engstuff.coloriphornia.data.Cv;

public class AppHelper {

    public static void startFullColorC(Activity activity, String rgb, String hex) {

        String[] colorParams = {rgb, hex};

        Intent i = new Intent(activity, FullScreenColorC.class);

        i.putExtra(Cv.EXTRA_MESSAGE_COLOR_1, colorParams);
        i.putExtra(Cv.EXTRA_MESSAGE_TEXT_COLOR_1, ColorParams.blackOrWhiteText(hex));

        activity.startActivity(i);
    }
}
